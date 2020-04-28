
# Ejemplo 6 - Servicio Base y Asociaciones muchos-a-uno

## Antencedentes

- Partimos del proyecto del [Ejemplo 5](https://github.com/lmlopezmagana/bbdd-2020/tree/master/05_PrimerProyectoDataJpa).
- En él creamos un nuevo proyecto de Spring Boot con las dependencias necesarias (web, data, h2, lombok)
- Configuramos nuestra conexión a base de datos a través del fichero de _properties_.
- Estructuramos algunos de los paquetes de nuestra aplicación.
- Construimos nuestra primera entidad, `Alumno`, añadiendo las anotaciones necesarias.
- Creamos también un repositorio para gestionar la entidad creada en el paso anterior.

## Servicio Base

Ya hemos comentado en ejemplos anteriores que una buena práctica para evitar el acoplamiento es no utilizar los repositorios directamente desde los controladores. En su lugar, es bueno implementar una capa de servicios que capture toda la lógica de negocio.

Los métodos CRUD (inserción de una nueva entidad, edición de una existente, borrado de una entidad, búsqueda de una entidad por su Id y la búsqueda de todas las entidades de un tipo) suelen ser muy repetitivos de implementar. De hecho, su codificación en el servicio es simplemente un envoltorio de los métodos correspondientes en el repositorio.

Por eso, es sensato proporcionar una implementación base de un servicio abstracto, que nos proporcione esta implementación, y nos permita centrarnos solo en nuestra lógica de negocio.

### Ejemplo de servicio base

Proponemos la siguiente implementación:

`IBaseService`
```java
public interface IBaseService<T, ID> {

	T save(T t);

	T findById(ID id);

	List<T> findAll();

	T edit(T t);

	void delete(T t);

	void deleteById(ID id);

}
```

`BaseService`
```java
public abstract class BaseService<T, ID, R extends JpaRepository<T, ID>> implements IBaseService<T, ID> {

	protected R repositorio;
	
	public BaseService(R repo) {
		this.repositorio = repo;
	}
	
	@Override
	public T save(T t) {
		return repositorio.save(t);
	}
	
	@Override
	public T findById(ID id) {
		return repositorio.findById(id).orElse(null);
	}
	
	@Override
	public List<T> findAll() {
		return repositorio.findAll();
	}
	
	@Override
	public T edit(T t) {
		return repositorio.save(t);
	}
	
	@Override
	public void delete(T t) {
		repositorio.delete(t);
	}
	
	@Override
	public void deleteById(ID id) {
		repositorio.deleteById(id);
	}
	
}
```

Utilizamos _genéricos_ para establecer los tipos de datos que necesitamos:

- `T`, el tipo de entidad que vamos a gestionar.
- `ID`, el tipo de dato su de Id (_clave primaria_).
- `R`, el tipo de dato de su repositorio (por ello utilizamos un parámetro de _tipo delimitado_).

### Creación de un nuevo servicio a partir del servicio base

La creación de un nuevo servicio a partir de este es ya muy sencilla

```java
@Service
public class AlumnoServicio extends BaseService<Alumno, Long, AlumnoRepository>{

	public AlumnoServicio(AlumnoRepository repo) {
		super(repo);
	}
	
}
```

> Si de verdad queremos mejorar esta solución, tendríamos que definir cada uno de estos servicios en una **interfaz** y una **implementación**. Con esto, también ganaríamos en desacoplar nuestros servicios del resto de capas, como los controladores.

### Limitaciones de esta solución

Una de las limitaciones que tiene es fácilmente subsanable. Se podría dar el caso de que el tipo `T` no fuese una entidad de nuestro modelo. Para solucionarlo, podríamos _forzar_ a que todas nuestras clases modelo implementaran una interfaz, y utilizar dicha interfaz para delimitar el tipo del parámetro `T`.

## Asociación _Muchos-A-Uno_

### ¿Qué vamos a crear?

1. Una nueva entidad, que está asociada con la anterior.
2. La asociación entre ambas entidades.

### ¿Cuál es nuestro modelo de datos?

![diagrama uml](./uml.jpg) 

### Algo de teoría sobre asociaciones _ManyToOne_

Una asociación _muchos-a-uno_ es una asociación que relaciona dos clases, de forma que una instancia del lado _uno_ (en nuestro caso, curso), puede asociarse con varias instancias del lado _muchos_, (en nuestro caso, los alumnos).

Para poder implementar esto en nuestra aplicación necesitamos:

1. Trasladar la entidad del lado _muchos_.
2. Trasladar la entidad del lado _uno_.
3. Añadir los elementos necesarios para implementar la asociación.

**¿Cómo impacta esto en nuestro sistema?**

Como norma general:

- La entidad del lado _muchos_ tendrá su repositorio y su servicio.
- La entidad el lado _uno_ también tendrá su repositorio y su servicio.

### Paso 1: Creamos la segunda entidad

Es muy parecida a la que ya tenemos creada

```java
@Entity
public class Curso {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String nombre;
	private String tutor;
	
	public Curso(String nombre, String tutor) {
		this.nombre = nombre;
		this.tutor = tutor;
	}	
}

```

### Paso 2: Creamos el repositorio y servicio para esta entidad

```java
public interface CursoRepository 
		extends JpaRepository<Curso, Long> {

}
```

```java
@Service
public class CursoServicio extends BaseService<Curso, Long, CursoRepository> {

	public CursoServicio(CursoRepository repo) {
		super(repo);
	}

}
```

## Paso 3: Modificación de la clase `Alumno` para registrar la asociación entre clases

Para añadir los elementos necesarios para poder registrar la asociación entre ambas clases tenemos que modificar nuestra clase `Alumno`. Necesitamos saber, para cada instancia de `Alumno`, cual es su instancia de `Curso` correspondiente. Por tanto, tenemos que añadir una propiedad a la primera clase, de este segundo tipo.

```java
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String nombre;
	private String apellidos;
	private String email;
	
	private Curso curso;
}
```

**Sin embargo, esto por sí solo no es suficiente**. JPA (en nuestro caso, Spring Data JPA) nos pide que identifiquemos esta asociación, para que la traslade al DDL (si corresponde) y la pueda manejar. Todo ello lo conseguimos a través de la anotación `@ManyToOne`.

El código, finalmente, quedaría así:

```java
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String nombre;
	private String apellidos;
	private String email;
	
	@ManyToOne
	private Curso curso;
	
	public Alumno(String nombre, String apellidos, String email) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
	}

	public Alumno(String nombre, String apellidos, String email, Curso curso) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.curso = curso;
	}

}

```

Si ejecutamos el proyecto, podemos apreciar el DDL generado

```sql
create table alumno (
	id bigint not null, 
	apellidos varchar(255), 
	email varchar(255), 
	nombre varchar(255), 
	curso_id bigint, 
	primary key (id)
);

create table curso (
	id bigint not null, 
	nombre varchar(255), 
	tutor varchar(255), primary key (id)
);

alter table alumno 
add constraint FKojks48ahsqwkx9o2s7pl0212p 
foreign key (curso_id) references curso;
```

Podemos apreciar como:

- Se añade un nuevo atributo, llamado `curso_id`, cuyo tipo es el mismo que el del Id de `Curso`.
- Se añade también una restricción de clave externa para este atributo.
