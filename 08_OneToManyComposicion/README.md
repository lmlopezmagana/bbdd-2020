
# Ejemplo 8 - Ejemplo una asociación _One To Many_ a través de una composición (fuerte-débil)

## Asociaciones de composición

Una asociación de composición puede venir representada por el siguiente diagrama

![Diagrama](./uml.jpg)

Esta asociación, como en ejemplos anteriores, es una asociación _uno-a-muchos_ que podemos tratar bidireccionalmente, para que su gestión sea más eficiente; sin embargo, el hecho de que sea de **composición le da un tinte algo especial**.

En esta asociación, el _componente_ es una entidad débil; no tiene sentido su existencia fuera del ámbito de una instancia de un _compuesto_. Por tanto, la gestión de cada componente debe ir asociada a la gestión del compuesto.

## Operaciones en cascada

JPA nos permite realizar operaciones en cascada con entidades. ¿Recuerdas las políticas de borrado en tablas en SQL? Viene a ser lo mismo, pero más potente, ya que no solo funcionan con operaciones de borrado, sino que se pueden usar con todas las operaciones: consultar, salvar, borrar, ...

**¿Cómo conseguimos hacer estas operaciones?** Las anotaciones como `@OneToMany` pueden recibir algunos argumentos además de `mappedBy`. Entre ellos están los siguientes:

- `Cascade`: podemos indicar qué tipo de operaciones en cascada queremos que se realicen al trabajar con esta entidad. Debe ser un valor de la enumeración `javax.persistence.CascadeType`, a saber: `ALL`, `PERSIST`, `MERGE`, `REMOVE`, `DETACH`.
- `orphanRemoval`: propiedad booleana que permite indicar que si una entidad a la que hace referencia la anotación de asociación (por ejemplo, `@OneToMany`) pierde _su clave externa_ (es decir, la entidad con la que está asociada, y por tanto _queda huérfana_) se eliminará.  

## Paso 1: Implementación de la asociación en el lado _muchos_


```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Componente {
	
	@Id @GeneratedValue
	private Long id;
	
	private String atr1;
	private int atr2;
	private double atr3;
	
	@ManyToOne
	private Compuesto compuesto;
	
	public Componente(String atr1, int atr2, double atr3) {
		this.atr1 = atr1;
		this.atr2 = atr2;
		this.atr3 = atr3;
	}
	
	

}
```

## Paso 2: Implementación de la asociación en el lado _uno_

```java
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Compuesto {
	
	@Id @GeneratedValue
	private Long id;
	
	private String prop1;
	private String prop2;
	private LocalDate fecha;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="compuesto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Componente> componentes = new ArrayList<>();

	public Compuesto(String prop1, String prop2, LocalDate fecha) {
		this.prop1 = prop1;
		this.prop2 = prop2;
		this.fecha = fecha;
	}
	
	
	/**
	 * MÉTODOS AUXILIARES Ó HELPERS
	 * 
	 */
	
	public void addComponente(Componente c) {
		c.setCompuesto(this);
		this.componentes.add(c);
	}
	
	
	public void removeComponente(Componente c) {
		this.componentes.remove(c);
		c.setCompuesto(null);
	}
	

}

```

## Paso 3: Sobre repositorios y servicios

Este caso, como hemos podido comprobar, es un poco especial, ya que la gestión del `Componente` la realizamos a través del compuesto. Por tanto, **no necesitamos implementar ni repositorio ni servicio para la clase `Componente`**. Tan solo lo necesitamos de compuesto.

### Cómo hacer un JOIN en Spring Data JPA

En asociaciones de este tipo Hibernate ofrece, en ocasiones, un rendimiento pobre, ya que cuando queremos obtener un `Compuesto` con todos sus componentes, realizará _n+1_ consultas SELECT. WTF!

Sí, para obtener una instancia del lado _compuesto_, y todos sus componentes hará:

- Una sentencia select para obtener el _compuesto_
- Una sentencia select para obtener cada uno de los componentes del listado.

Esto es francamente ineficiente. Lo podemos solventar creando nuestra primera consulta en un repositorio, quedando el código así:

```java
public interface CompuestoRepository 
			extends JpaRepository<Compuesto, Long> {
	
	@Query("select distinct c from Compuesto c join fetch c.componentes")
	List<Compuesto> findAllJoin();

}

```

Más adelante entraremos a explicar las consultas en profundida.

Para que nuestro servicio utilice la consulta anterior, en lugar de la consulta `findAll()` por defecto del repositorio, **tenemos que sobrescribir el método correspondiente**:

```java
@Service
public class CompuestoServicio extends BaseService<Compuesto, Long, CompuestoRepository> {

	public CompuestoServicio(CompuestoRepository repo) {
		super(repo);
	}

	@Override
	public List<Compuesto> findAll() {
		return this.repositorio.findAllJoin();
	}
	
	

}
```


## Paso 4: Cómo trabajar con ahora con `Compuesto` y `Componente`

Para trabajar ahora con nuestras entidades asociadas, tendremos que usar los métodos que hemos definido en los pasos anteriores

```java
			// Al ser una composición, y los componentes no
			// tener "entidad suficiente" (entidades débiles)
			// todas las operaciones se realizan a través
			// de la entidad fuerte (Compuesto)
			
			Compuesto compuesto = new Compuesto("Valor 1", "Valor 2", LocalDate.now());
			
			// Podemos construir los compuestos directamente al añadir
			compuesto.addComponente(new Componente("v1", 2, 3.0));
			
			// También podemos construirlos fuera, y añadir la referencia
			Componente componente2 = new Componente("v2", 4, 6.0);
			compuesto.addComponente(componente2);

			// Miremos qué sucede en el log al almacenar el componente
			servicio.save(compuesto);
			
			// Añadimos otro compuesto con sus componentes
			Compuesto otroCompuesto = new Compuesto("Cadena 1", "Cadena 2", LocalDate.of(1990, 1, 1));
			otroCompuesto.addComponente(new Componente("One", 2, 3.0));
			otroCompuesto.addComponente(new Componente("Cuatro", 5, 6.0));
			otroCompuesto.addComponente(new Componente("Siete", 8, 9.0));
			servicio.save(otroCompuesto);
			
			
			// ¿Qué consulta SQL se ejecuta al obtener la lista de todos los compuestos?
			List<Compuesto> result = servicio.findAll();
			
			System.out.println("\n");
			for(Compuesto c : result) {
				System.out.println("Compuesto: " + c);
				System.out.println("Componentes: " + c.getComponentes());
			}


```



