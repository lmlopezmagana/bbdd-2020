# Ejemplo 21 - Uso de beans de sesión (para implementar un carro de la compra).

## Algo de teoría

Uno de los primeros conceptos que se estudian en programación es el **ámbito** de una variable, que no es otra cosa que "su tiempo y espacio de vida", es decir, desde qué parte del código y durante cuánto tiempo tenemos acceso a ella. Este concepto de **ámbito** también se puede aplicar a los beans, como referencias a objetos que son. Spring Framework nos ofrece una gestión sencilla del ámbito de beans.

### Ámbitos más comunes

Si no indicamos lo contrario, un bean tiene por defecto un ámbito _sigleton_; es decir, solo se crea una instancia de dicho objeto, y cada vez que inyectamos una dependencia de ese tipo de objeto, se nos devuelve una referencia al mismo.

![Singleton](https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/images/singleton.png)

Si este tipo de ámbito no nos sirve, podemos definir nuestro bean para que tenga un ámbito _prototype_; cada vez que inyectemos una dependencia de este tipo, se instanciará un nuevo objeto.

![Prototype](https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/images/prototype.png)

### Ámbitos web

Si nuestra aplicación es una aplicación web, tenemos disponibles algunos ámbitos más:

- `Request`: el ámbito del bean se limita al ciclo de vida de una petición.
- **`Session`: este tipo de beans tendrá como ámbito toda una sesión**.
- `Application`: similar al singleton; el ámbito se limita al ciclo de vida de un _ServletContext_. 

![Ámbitos web](https://confluence.sakaiproject.org/download/attachments/44793969/ScopeImage.png?version=2&modificationDate=1163380496000&api=v2)

> Podemos definir una sesión como el tiempo que hay desde que un usuario _se conecta_ a nuestra aplicación y _se desconecta_.

## Carro de la compra

Usualmente, muchas webs permiten a los usuarios ir agrupando una serie de productos que quieren comprar, para después poder procesar su compra. Se trata de una metáfora de entrar físicamente en un supermercado, e ir metiendo e una cesta o un carro los productos que queremos comprar. Meterlos dentro del carro no implica que ya estén comprados. Hasta que no pasemos por caja, y se procese la compra de todos los productos, esos productos no estarán definitivamente comprados.

Como alternativas para almacenar esa cesta de la compra tenemos:

- *Almacenarlo en la base de datos a través de entidades*: esto nos permite al usuario dejar una compra a medias, _cerrar la sesión_, volver a iniciar sesión y poder seguir con la misma compra. Como inconveniente, está que se genera un gran tráfico con la base de datos que, posiblemente, no termine en una compra definitiva.
- **Almacenarlo en un bean de sesión**: esto permite al usuario continuar con la compra mientras la sesión esté activa (lo cual puede ser bastante tiempo), sin tener que generar tráfico innecesario con la base de datos. En este ejemplo tomamos esta segunda alternativa.

## Cómo trabajar con sesiones en Spring

Tenemos dos alternativas

- Trabajar con objetos de tipo `HttpSession`: se puede inyectar en un controlador una referencia a un objeto de tipo [javax.servlet.http.HttpSession](https://docs.oracle.com/javaee/7/api/?javax/servlet/http/HttpSession.html). Se trata de una interfaz definida en Java EE.
- Crear nuestro propio bien anotado con `@SessionScope`. Este bean podrá ser estereotipado como servicio, y nos permitirá definir atributos y métodos propios.

> En este ejemplo escogemos la segunda alternativa.


## Código inicial

Partimos de la base de que vamos a trabajar con un conjunto de `Producto`s sencillos que queremos permitir que sean comprados:

```java
@Data @NoArgsConstructor
@Entity
public class Producto {
	
	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private float precio;
	
	public Producto(String n, float p) {
		this.nombre = n;
		this.precio = p;
	}
	
}

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}


@Service
public class ProductoServicio extends BaseService<Producto, Long, ProductoRepository>{

	public ProductoServicio(ProductoRepository repo) {
		super(repo);
	}
	
	
}
```

Los productos se mostrarán en una plantilla a través del siguiente controlador

```java
@Controller
@RequiredArgsConstructor
public class ProductoController {
	
	private final ProductoServicio productoServicio;
	
	@ModelAttribute("productos")
	public List<Producto> todosLosProductos() {
		return productoServicio.findAll();
	}
	
	@GetMapping({"/", "/list"})
	public String index(Model model) {
		return "index";
	}
	
    // Resto del código
}
```

> Recuerdo que los métodos anotados con `@ModelAttribute` nos permiten tener disponible un objeto en cualquier método del controlador, como si lo hubiéramos pasado con `model.addAtributte(...)`.

## Pasos para implementar el `Carrito`

### Operaciones a incluir

Podemos definir una interfaz con las operaciones de las que nos tiene que proveer un carrito:

```java
public interface Carrito {

	
	void addToCarrito(Producto p);
	void addToCarrito(long id);

	void removeFromCarrito(Long id);
    void removeFromCarrito(Producto p);
	
	void clear();
	
	Map<Producto, Integer> getCarrito();

	int numeroTotalProductosDiferentes();
	int numeroTotalDeUnidades();
	float importeTotal();

}
```

### Colección a utilizar

En este punto tenemos diferentes alternativas:

- Utilizar un `Map<Long, Integer>`: nos permite guardar el ID de los productos que se quieren comprar, así como el número de unidades.
- Utilizar un `Map<Producto, Integer>`: nos permite guardar la referencia al producto a comprar, así como el número de unidades. **Escogemos esta versión**. Como ventaja es que utilizamos una colección predefinida por Java.
- Utilizar un `List<LineaCarrito>`, donde `LineaCarrito` incluya la referencia a `Producto` y el número de unidades. La ventaja de este enfoque es el uso de una colección lineal; además, a la clase `LineaCarrito` se le podrían añadir algunos métodos, como `subTotal`.

### Implementación de las operaciones

Por tanto, partimos de un código como este:

```java

@Service
@SessionScope
@RequiredArgsConstructor
public class CarritoImpl implements Carrito {

	private final ProductoServicio productoServicio;
    private Map<Producto, Integer> productosEnCarrito = new LinkedHashMap<>();

}
```

> Escogemos `LinkedHashMap` porque nos ayuda a mantener el orden de inserción de los productos.

A partir de aquí tenemos que ir incluyendo la implementación de los diferentes métodos:

```java
@Override
public void addToCarrito(Producto p) {
	if (productosEnCarrito.containsKey(p)) {
		productosEnCarrito.put(p, productosEnCarrito.get(p)+1);
	} else {
        productosEnCarrito.put(p, 1);
	}
}
	
@Override
public void addToCarrito(long id) {
	Producto producto = productoServicio.findById(id);
	if (producto != null) {
		addToCarrito(producto);
	}
}
```

Ofrecemos dos implementaciones para añadir un `Producto` al carrito: usando la referencia, o usando su ID.

```java
@Override
public void removeFromCarrito(Long id) {
	Producto producto = productoServicio.findById(id);
	if (producto != null) {
		removeFromCarrito(producto);
	}
}
	
@Override
public void removeFromCarrito(Producto p) {
	productosEnCarrito.remove(p);
}

@Override
public void clear() {
	productosEnCarrito.clear();
		
}
```

Hacemos lo mismo para eliminar un `Producto` del carrito; además, ofrecemos la posibilidad de vaciarlo por completo.


```java

@Override
public int numeroTotalProductosDiferentes() {
	return productosEnCarrito.size();
}
	
	
@Override
public int numeroTotalDeUnidades() {
	int acumulador = 0;
	for(Producto p : productosEnCarrito.keySet()) {
		acumulador += productosEnCarrito.get(p);
	}
	return acumulador;
}
	
	
@Override
public float importeTotal() {
	float acumulador = 0.0f;
	for(Producto p : productosEnCarrito.keySet()) {
		acumulador += productosEnCarrito.get(p) * p.getPrecio();
	}
	return acumulador;		
}
```

Ofrecemos algunos métodos de utilidad que nos permitan obtener el número de productos diferentes, el número total de unidades o el importe total de lo que se ha acumulado en el carrito.

```java
@Override
public Map<Producto, Integer> getCarrito() {
	return Collections.unmodifiableMap(productosEnCarrito);
}
```

Por último, ofrecemos el carrito como una versión inmutable del `Map<Producto, Integer>`

> Ofrecerlo como una versión inmutable permite evitar modificaciones del `Map` desde fuera del carrito.

### Controlador y plantillas

Necesitamos implementar las siguientes operaciones en un controlador:

- Añadir un producto al carro
- Eliminar un producto del carro
- Consultar el carro de la compra
- Vaciar el carro de la compra
- Procesar el carro de la compra (transformarlo en una compra almacenada en base de datos)

En este ejemplo lo implementamos en el mismo controlador de `Producto`:

```java

@Controller
@RequiredArgsConstructor
public class ProductoController {
	
	private final ProductoServicio productoServicio;
	private final Carrito carrito;
	
	//Resto del código
	
	@GetMapping("/carrito/add/{id}")
	public String addToCart(@PathVariable("id") Long id) {
		carrito.addToCarrito(id);	
		return "redirect:/carrito/mostrar";
	}

	@GetMapping("/carrito/mostrar")
	public String showCart(Model model) {
		model.addAttribute("carrito", carrito.getCarrito());
		model.addAttribute("numProductosDiferentes", carrito.numeroTotalProductosDiferentes());
		model.addAttribute("importeTotal", carrito.importeTotal());
		model.addAttribute("numUnidades", carrito.numeroTotalDeUnidades());
		return "carrito";
	}
	
	@GetMapping("/carrito/remove/{id}")
	public String removeFromCart(@PathVariable("id") Long id) {
		carrito.removeFromCarrito(id);
		return "redirect:/carrito/mostrar";
	}
	
	@GetMapping("/carrito/clear")
	public String clearCart() {
		carrito.clear();
		return "redirect:/";
	}
	
	@GetMapping("/carrito/process")
	public String procesarCarrito() {
		
		// En este método habría que conectar con otro servicio
		// para transformar los datos del carrito en una compra de
		// verdad. 
		
		// TODO Procesamiento de productos
		
		// Posteriormente, habría que vaciarlo y redirigir al usuario donde corresponda
		carrito.clear();
		return "redirect:/";
		
	}
	
	
}
```

En la plantilla `index.html`, para añadir los productos al carrito, utilizamos enlaces a la ruta `/carrito/add/{id}`:

```html
			<tr th:each="prod : ${productos}">
				<td th:text="${prod.nombre}">Nombre</td>
				<td style="text-align: right;" th:text="${#numbers.formatCurrency(prod.precio)}">1.23€</td>
				<td><a th:href="@{/carrito/add/{id}(id=${prod.id})}">Añadir al carrito</a></td>
			</tr>
```

En la plantilla `carrito.html`, tendríamos el siguiente código:

```html
<body>
	<h1>Productos en carrito</h1>
	<p>Nº de productos en carrito <span th:text="${numProductosDiferentes}"></span></p>
	<p>Nº de unidades en carrito <span th:text="${numUnidades}"></span></p>	
	<table>
		<thead>
			<tr>
				<th>Unidades</th>
				<th>Nombre</th>
				<th>Precio unitario</th>
				<th>Subtotal</th>
				<th>Operaciones</th>
		</thead>
		<tbody>
			<tr th:each="entry : ${carrito}">
				<td th:text="${entry.value}">1</td>
				<td th:text="${entry.key.nombre}">Nombre</td>
				<td style="text-align: right;" th:text="${#numbers.formatCurrency(entry.key.precio)}">1.23€</td>
				<td style="text-align: right;" th:text=${#numbers.formatCurrency(entry.key.precio*entry.value)}></td>
				<td><a th:href="@{/carrito/remove/{id}(id=${entry.key.id})}">Eliminar del carrito</a></td>
			</tr>
		</tbody>
	</table>
	<p>Importe total de los productos <span th:text="${#numbers.formatCurrency(importeTotal)}"></span></p>
	<p><a th:href="@{/}">Seguir comprando</a> | <a th:href="@{/carrito/clear}">Vaciar carrito</a></p>
	<p><a th:href="@{/carrito/process}">FINALIZAR COMPRA</a></p>
</body>

```

> Nótese que, dado que el carrito es un `Map<Producto, Integer>`, estamos iterando de él mediante `th:each="entry : ${carrito}`. Esto es equivalente a usar el método `Map.entrySet()`, que devuelve una colección de `Map.Entry<K,V>`. Cada instancia tiene un `key` (`Producto`) y un `value` (`Integer`). 
