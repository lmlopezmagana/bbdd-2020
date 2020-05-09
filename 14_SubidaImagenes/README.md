
# Ejemplo 14 - Subida de imágenes

En este ejemplo vamos a implementar un servicio conveniente para la subida de imágenes, utilizándolo desde un formulario _multiparte_.

## Algo de teoría

Al crear un formulario en HTML, tenemos que indicar normalmente un atributo, que indica de qué manera se va a _codificar_; en palabras llanas, de qué forma se va a procesar la información para poder enviarla al servidor. Esto suele venir delimitado por el atributo `enctype` y los posibles valores son:

- `text/plain`
- `application/x-www-form-urlencoded`: es la opción por defecto
- **`multipart/form-data`**: la que nos interesa ahora.

Mediante la elección de `multipart/form-data` le estamos indicando a nuestra aplicación que el formulario que enviemos puede tener varias partes (de ahí la palabra _multipart_), y cada parte podrá tener un tipo de contenido distinto. Esto nos permite que podamos enviar _de una sola vez_ campos de texto y ficheros binarios.

He aquí un ejemplo de formulario multiparte:

```html
<form method="post" enctype="multipart/form-data" action="/ruta/de/procesamiento">
	<input type="text"d id="nombre" placeholder="Nombre"  /> 
	<input id="filebutton" name="file" type="file">
	<button type="submit">Enviar</button>
</form>
```

## Más teoría: uso de _properties_ y `@Value`

En muchas ocasiones querremos poder configurar determinados aspectos de nuestra aplicación sin necesidad de tocar el código fuente. Una buena manera de hacerlo es a través del sistema de _properties_, como el fichero `application.properties`. Spring Boot proporciona todo lo necesario para que podemos utilizar en él propiedades _predefinidas_ (conocidas por Spring), o incluso algunas de nuestra cosecha. Con esto, podemos definir el valor de algunas variables que luego podemos utilizar a lo largo del sistema.

Hay diferentes formas de conseguir el valor de una propiedad, pero uno de los más sencillos es a través de `@Value` (**ojo con la clase que hay que importar**: `org.springframework.beans.factory.annotation.Value`). Esta anotación nos permite inyectar el valor de una propiedad en una variable. Lo más seguro es hacerlo a través de un constructor.


```java
@Service
public class DBStorageService implements StorageService<ImagenEntity, Long> {
		
	private float compressionQuality;
		
	public DBStorageService(
			@Value("${image.compression-quality:0.5}") float cQ,
			//...) {
		this.compressionQuality = cQ;
		// ...
	}
```

De esta forma:

- Al crearse el bean, se inyecta el valor de la propiedad `image.compression-quality` en la variable `cQ`.
- Si dicha propiedad no está definida en `application.properties`, se le da el valor por defecto que viene después de los dos puntos (0.5)
- Se asigna ese valor a `compressionQuality`.

### Procesamiento multiparte en Spring MVC

Para procesar un formulario _normal_ en Spring, tenemos la anotación `@ModelAttribute`, que nos permite obtener todos los campos del formulario en un objeto (llamado _Command Object_ aunque también se le conoce como _form bean_). Sin embargo, para poder manejar formularios multiparte, esto no es suficiente. Necesitamos hacer uso de la clase `org.springframework.web.multipart.MultipartFile` que nos permite gestionar los ficheros que se han adjuntado desde el formulario.

> En este ejemplo se proporciona todo el código necesario para almacenar los ficheros recibidos a través de una instancia de `MultipartFile`. Tan solo tienes que integrar este código en tu proyecto.


## Código que se proporciona para el servicio de almacenamiento

Todo el código que se proporciona está basado en un ejemplo [_oficial_ de Spring](https://spring.io/guides/gs/uploading-files/), donde se propone un servicio de almacenamiento, con una implementación basada en el sistema de ficheros. Aquí se propone una para almacenar las imágenes codificadas como cadenas de caracteres en base64 entro de una tabla de la base de datos.

> Vaya por delante que esta implementación no es adecuada para sistemas en producción. Sin embargo, nos va a permitir familiarizarnos con la subida de ficheros y, en próximos proyectos, podremos hacer la implementación basada en el sistema de ficheros, en la nube, ...

### Servicio de subida

El servicio viene definido a través de la siguiente interfaz:

```java
public interface StorageService<T, U> {

    void init();

    String store(MultipartFile file);

    Stream<T> loadAll();

    T load(U id);

    Resource loadAsResource(U id);
    
    void delete(U id);

    void deleteAll();

}
``` 

Los tipos de datos que se deben proporcionar indican:

- `T`: qué obtenemos al guardar una imagen. 
- `U`: de qué manera se identifica la imagen. 

La implementación para almacenar en base de datos la explicamos poco a poco

#### Cabecera de la clase:

```java
@Service
public class DBStorageService implements StorageService<ImagenEntity, Long> {
	
	
	private float compressionQuality;
	
	private int maxSize;

	private final ImagenEntityRepository repository;
	
	public DBStorageService(
			@Value("${image.compression-quality:0.5}") float cQ,
			@Value("${image.max-size:1024}") int mS,
			ImagenEntityRepository repository
			) {
		this.compressionQuality = cQ;
		this.maxSize = mS;
		this.repository = repository;
	}

	private final Logger logger = LoggerFactory.getLogger(DBStorageService.class);

    // Resto del código
}    
```

- La clase `DBStorageService` es un servicio, e implementa a la interfaz explicada anteriormente
- Los tipos elegidos son
    - `ImagenEntity`: una entidad que nos sirve para almacenar en la base de datos el contenido de la imagen en base64
    - `Long`: vamos a identificar cada imagen a través del id de `ImagenEntity`, definido como este tipo.
- En el constructor, inicializamos algunos argumentos necesarios.:
    - El repositorio de `ImagenEntity`
    - El factor de compresión de la imagen. Es un `float` entre `0` y `1`.
    - El tamaño máximo (en pixels) del lado más grande de la imagen. Por defecto es `1024`.

#### Métodos más interesantes

```java
@Service
public class DBStorageService implements StorageService<ImagenEntity, Long> {

    @Override
	public String store(MultipartFile file) {
		if (file.isEmpty()) {
			logger.debug("Se ha subido un fichero vacío");
			return null;
		}

		try {
			String base64img = Base64.getEncoder().encodeToString(compressImg(file.getBytes(), file.getContentType()));
			ImagenEntity saved = repository.save(new ImagenEntity(base64img));
			return Long.toString(saved.getId());
		} catch (IOException e) {
			logger.error("Error al transformar el fichero a base64");
			logger.error(e.getMessage());
			return null;
		}
	}

}
```

Este método es el que utilizaremos para almacenar las imágenes recibidas a través de un `MultipartFile`. Se encarga de:

- Comprobar si la imagen tiene contenido. Si es vacía devuelve `null`.
- Comprimir y escalar la imagen.
- Almacenarla en la base de datos.
- Devolver el ID de la misma **(este ID es el que tendremos que guardar en algún campo de nuestro modelo)**.


```java
@Service
public class DBStorageService implements StorageService<ImagenEntity, Long> {

    @Override
	public Resource loadAsResource(Long id) {

		ImagenEntity imagen = load(id);
		if (imagen != null) {
			byte[] byteImage = Base64.getDecoder().decode(imagen.getContenido());

			Resource resource = new ByteArrayResource(byteImage);

			if (resource.exists() || resource.isReadable()) {
				return resource;
			}

		}

		return null;
	}
```

Este método nos permite **abstraernos** de cómo están almacenadas las imágenes, y obtenerlas como un recurso (`Resource`). Será utilizado por un controlador especial para proporcionarnos lo necesario para cargar las imágenes en nuestras plantillas.


## ¿Cómo utilizar este código?

**Tienes que integrar en tu proyecto todo lo que encuentres en el paquete `upload`y sus subpaquetes.**

A partir de ahí:

1) Puedes añadir en el fichero `application.properties` los siguientes valores:
    - `image.max-size`: por defecto se tomará `1024`.
    - `image.compression-quality`: por defecto se tomará 0.5 (ten en cuenta que cuanto más cerca de 1, menor compresión, y cuanto más cerca de 0, mayor compresión.)
2) Seguro que las imágenes las almacenas asociadas a una entidad de tu modelo (o incluso a varias; de ser así, tienes que repetir este punto por cada entidad). En dicha entidad añade un campo imagen, de tipo `String`

```java
@Data @NoArgsConstructor
@Entity
public class CosaConImagen {
	
	@Id @GeneratedValue
	private long id;
	
	private String nombre;
	
	private String imagen;
		
	public CosaConImagen(String nombre) {
		this.nombre = nombre;
	}

}
```

- Como es natural, esta entidad tendrá servicio y repositorio, como siempre.
- El servicio de la entidad puede heredar de `BaseService`, como solemos hacer. Hay que añadir algunos métodos más
    - `save(Entidad e, MultipartFile imagen)`: este debe realizar el almacenamiento de nuestra entidad con la imagen.
    - Sobreescribir los métodos de borrado: cuando eliminemos una entidad, se debe borrar la imagen asociada.
    - Posiblemente también tengamos que modificar el método de edición, para realizar cambios en la imagen asociada:

```java
@Service
public class CosaConImagenServicio 
		extends BaseService<CosaConImagen, Long, CosaConImagenRepository>{
	
	
	private final DBStorageService dbStorageService;

	public CosaConImagenServicio(CosaConImagenRepository repo, 
			DBStorageService dbStoreService
			) {
		super(repo);
		this.dbStorageService = dbStoreService;
	}
	
	/**
	 * Método que permite guardar una entidad que tiene una imagen.
	 * 
	 * @param c
	 * @param imagen
	 * @return
	 */
	public CosaConImagen save(CosaConImagen c, MultipartFile imagen) {
		// Pasos a seguir
		
		// 1) Transformar la imagen en un String
		String pathImagen = dbStorageService.store(imagen);
		// 2) Asignar esta cadena de caracteres con nuestra entidad
		c.setImagen(pathImagen);
		// 3) Almacenarla
		return this.save(c);
	}

	/**
	 * Antes de eliminar nuestra entidad, tenemos que eliminar la imagen asociada.
	 */
	@Override
	public void delete(CosaConImagen t) {
		String idImagen = t.getImagen();
		dbStorageService.delete(Long.valueOf(idImagen));
		super.delete(t);
	}

	/**
	 * Antes de eliminar nuestra entidad, tenemos que eliminar la imagen asociada.
	 */
	@Override
	public void deleteById(Long id) {
		CosaConImagen cosa = findById(id);
		if (cosa != null)
			delete(cosa);
	}
	
}

```

3. Para implementar la subida del fichero, necesitas dos métodos en el controlador: uno `GET` y otro `POST`:

- La inyección de la propiedad `image.base-path` te permitirá modificar la ruta donde se _sirven_ los ficheros. Si no quieres, no tienes porque modificarla.
- El método anotado con `@ModelAttribute` porporciona esa ruta raíz a todas las plantillas de este controlador.

```java
@Controller
public class MainController {

	
	private final CosaConImagenServicio servicio;
	private final String BASE_IMAGE_PATH;
	
	
	public MainController(CosaConImagenServicio servicio, @Value("${image.base-path:/files}") String path) {
		this.servicio = servicio;
		this.BASE_IMAGE_PATH = path;
	}
	
	@ModelAttribute("base_image_path")
	public String baseImagePath() {
		return this.BASE_IMAGE_PATH;
	}
	
	
	@GetMapping("/")
	public String showForm(Model model) {
		model.addAttribute("cosa", new CosaConImagen());
		return "index";
	}
	
	
	@PostMapping("/submit")
	public String processForm(@ModelAttribute CosaConImagen cosaconImagen, MultipartFile file) {
		
		if (!file.isEmpty())
			servicio.save(cosaconImagen, file);
		else 
			servicio.save(cosaconImagen);
		
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("lista", servicio.findAll());
		return "list";
	}
	
	
}
```

- El método `showForm` proporciona, a través del modelo, un objeto para almacenar los valores del formulario.
- El código del formulario sería este. Incluye también un campo para subida de ficheros:

```html
	<form method="post" enctype="multipart/form-data" action="#"
		th:action="@{/submit}" th:object="${cosa}">

		<input type="text" id="nombre"
			placeholder="Nombre" th:field="*{nombre}" /> 
			
		<input id="filebutton"
			name="file" type="file">

		<button type="submit">Enviar</button>
	</form>

```

- El método `processForm` es el más interesante:
    - Recibe el `@ModelAttribute` con los datos del formulario.
    - También recibe el fichero a través de un objeto `MultipartFile`
    - Utiliza nuestro servicio _modificado_ para almacenar nuestra entidad con el fichero asociado.

4. La plantilla en la que podemos ver los ficheros utiliza expresiones de urls `@{...}`

```html
	<tbody>
		<tr th:each="c : ${lista}">
			<td th:text="${c.nombre}"></td>
			<td><img th:src="@{{base}/{id}(base=${base_image_path},id=${c.imagen})}" /></td>
		</tr>
	</tbody>

```
Si nos fijamos, la ruta sería `@{{base}/{id}(base=${base_image_path},id=${c.imagen})`, es decir:

- Se sustituye `{base}` por el valor de la expresión `${base_image_path}`, que es `/files`
- Después viene otra barra `/`
- Por último, se sustituye `{id}` por el valor de `${c.imagen}` para la entidad concreta de la lista sobre la que iteramos.
- Un valor de ejemplo del resultado de esta ruta sería `/files/7`. 

> A través del controlador `ImageController`, se realizan las operaciones necesarias para cargar esta imagen de la base de datos, traducirla de Base64 a un `Resource` y _servirla_ a nuestra navegador, _pintándose_ en pantalla.