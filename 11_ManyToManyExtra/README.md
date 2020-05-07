
# Ejemplo 11 - Ejemplo una asociación _Many To Many_ con atributos extra

Partimos desde el ejemplo [many-to-many bidireccinoal](https://github.com/lmlopezmagana/bbdd-2020/tree/master/09_ManyToManyBidi) con algunas modificaciones

## ¿Cuál es nuestro modelo de datos?

![diagrama uml](./uml.jpg) 

En este caso, podemos apreciar una clase de asociación muchos a muchos, que nos permite reflejar que un alumno se matricula en muchas asignaturas, así como sus notas.

## Asociación `@ManyToMany` con atributos extra

La implementación de una asociación _muchos-a-muchos_ simple ha resultado ser muy sencilla (incluso en el tratamiento bidireccional). Sin embargo, para **añadir atributos extra**, necesitamos crear una nueva entidad, y hacer uso de algunas anotaciones nuevas.

## Algo de teoría

### Tipos `Embeddable`

En alguna ocasión, puede que interesarnos agrupar un cierto conjunto de valores: supongamos que queremos manejar la `Localizacion` de una determinada `Oficina`. Una localización está formada por una `direccion`, una `ciudad` y un `pais`. Realmente no queremos tratar una `Localizacion` como una entidad; su ciclo de vida siempre estará asociado al de la `Oficina` correspondiente. Nos puede interesar agruparlo todo, por ejemplo, para dar un tratamiento integral. 

JPA nos ofrece la anotación `@Embeddable`, que nos permite generar una clase que será _encajable_ ( _incrustable_, _embebible_) en otra entidad.

```java
@Embeddable
public class Localizacion {

	private String direccion;
	
	private String ciudad;
	
	private String pais;

	// Resto del código
}

@Entity
public class Oficina {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Localizacion localizacion;
	
	private String telefonoContacto;
	
	// Resto del código

}
```

Como podemos observar, un elemento interesante de la clase anotada con `Embeddable` es que no tiene un atributo identificador (`@Id`).

El DDL generado sería algo parecido a esto:

```sql
create table Oficina (
	id bigint not null,
	direccion varchar(255),
	ciudad varchar(255),
	pais varchar(255),
	telefonoContacto varchar(255),
	primary key (id)
)	
```

### Identificadores compuestos

Hasta ahora, todas nuestras entidades han estado identificadas mediante un _identificador_ (valga la redundancia) que ha sido simple. De hecho, siempre hemos utilizado el siguente fragmento de código:

```java
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private long id;
```

En determinados contextos podemos necesitar otro tipo de identificador, compuesto por más de un atributo. 

JPA nos ofrece alguna estrategia para poder implementar esto, siempre y cuando se cumplan las siguientes reglas:

- El identificador compuesto debe estar representado por una _clase de clave primaria_. Esta se puede definir con las anotaciones `@EmbeddedId` o con la anotación `@IdClass`.
- La _clase de clave primaria_ debe ser pública y debe tener un constructor público sin argumentos.
- La _clase de clave primaria_ debe ser serializable.
- La _clase de clave primaria_ debe definir los métodos `equals` y `hashCode` de forma consistente.

Los atributo que forman esta composición pueden ser básicos, compuestos o anotados con `@ManyToOne`.  

Veamos el siguiente [ejemplo de la documentación de Hibernate](http://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#identifiers-composite-aggregated)

```java
@Entity(name = "SystemUser")
public static class SystemUser {

	@EmbeddedId
	private PK pk;

	private String name;

	//Getters and setters are omitted for brevity
}

@Embeddable
public static class PK implements Serializable {

	private String subsystem;

	private String username;

	public PK(String subsystem, String username) {
		this.subsystem = subsystem;
		this.username = username;
	}

	public PK() {
	}

}
```

### Uso de la anotación `@MapsId`

JPA 2.0 agregó soporte para identificadores derivados que permiten que una entidad tome prestado el identificador de una asociación de muchos a uno o de uno a uno. La anotación `@MapsId` también puede hacer referencia a columnas de un identificador `@EmbeddedId` (como va a ser nuestro caso).

## Implementación de la solución

### ¿Qué tenemos hasta ahora?

- Entidad `Alumno` con su repositorio y servicio
- Entidad `Curso` con su repositorio y servicio
- Entidad `Asignatura`, con su repositorio y servicio

También las asociaciones entre `Alumno` y `Curso` y entre `Curso` y `Asignatura`.

### Paso 1: Creamos la nueva clase que va a _mapear_ la clave primaria de la asociación _muchos-a-muchos_.

```java
@Data @NoArgsConstructor
@Embeddable
public class NotasPK implements Serializable {
	
	private static final long serialVersionUID = 8682909319466153524L;
	
	long alumno_id;
	
	long asignatura_id;	

}

```

Como podemos comprobar, cumplimos con las reglas estipuladas por JPA:

- Clase pública
- Implementa serializable
- Gracias a Lombok, tenemos un constructor público sin argumentos, y la implementación de `equals` y `hashCode`.

### Paso 2: Creamos la nuevo entidad que va a _mapear_ la asociación _muchos-a-muchos_.

```java

@Data @NoArgsConstructor
@Entity
public class MatriculaNotas {
	
	@EmbeddedId
	private NotasPK id = new NotasPK();
	
	@ManyToOne
	@MapsId("alumno_id")
	@JoinColumn(name="alumno_id")
	private Alumno alumno;	
	
	
	@ManyToOne
	@MapsId("asignatura_id")
	@JoinColumn(name="asignatura_id")
	private Asignatura asignatura;
	
	private int primeraEv;
	private int segundaEv;
	private int terceraEv;
	private int notaFinal;
	
	
	public MatriculaNotas(Alumno alumno, Asignatura asignatura, int primeraEv, int segundaEv, int terceraEv, int notaFinal) {
		this.alumno = alumno;
		this.asignatura = asignatura;
		this.primeraEv = primeraEv;
		this.segundaEv = segundaEv;
		this.terceraEv = terceraEv;
		this.notaFinal = notaFinal;
	}
	
	public MatriculaNotas(Alumno alumno, Asignatura asignatura) {
		this.alumno = alumno;
		this.asignatura = asignatura;
	}
	

}
```

Como podemos observar, vamos a **romper nuestra asociación `@ManyToMany` para utilizar dos conjuntos de asociaciónes  `@ManyToOne` + `@OneToMany`.** En esta entidad tenemos las asociaciones `@ManyToOne`.

Como hemos visto anteriormente en la teoría, podemos destacar dos cosas:

- Usamos `@EmbeddedId` para marcar la clave primaria (en lugar de usar `@Id`, como veníamos haciendo hasta ahora`).
- Marcamos los campos de tipo `Alumno` y `Asignatura` con `@MapsId`. Con esto conseguimos vincular cada campo con una parte de la clave primaria, y son las claves externas de una asociación _muchos-a-uno_.

### Paso 3: Modificación de las clases `Alumno` y `Asignatura`.

Ahora, tenemos que modificar ambas clases para transformar nuestra asociación `@ManyToMany` en el conjunto `@ManyToOne` + `@OneToMany`. En `Alumno` y `Asignatura` irán los extremos `@OneToMany`.

```java
@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String nombre, apellidos, email;
	
	@ManyToOne
	private Curso curso;
	
	// Mantenemos esta lista, pero no añadimos helpers
	// Si queremos rellenar la lista, realizamos un JOIN FETCH
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="alumno", fetch = FetchType.EAGER)
	private List<MatriculaNotas> matriculaNotas = new ArrayList<>();
	
	
	public Alumno(String n, String a, String e) {
		this.nombre = n;
		this.apellidos = a;
		this.email = e;
	}
	

}

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Asignatura {
	
	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private String profesor;
	
	@ManyToOne
	private Curso curso;
	
	// Mantenemos esta lista, pero no añadimos helpers
	// Si queremos rellenar la lista, realizamos un JOIN FETCH
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="asignatura", fetch = FetchType.EAGER)
	private List<MatriculaNotas> matriculaNotas = new ArrayList<>();
	
	public Asignatura(String nombre, String profesor) {
		this.nombre = nombre;
		this.profesor = profesor;
	}

	public Asignatura(String nombre, String profesor, Curso curso) {
		this.nombre = nombre;
		this.profesor = profesor;
		this.curso = curso;
	}
	
	

}

```

> Como podemos observar, damos un tratamiento bidireccional a la asociación a medias, ya que no implementamos los métodos helpers. Estas colecciones nos servirá si realizamos un JOIN FETCH entre `Alumno` o `Asignatura` y `MatriculaNotas`.

### Paso 4: Creación del repositorio y servicios para la clase `MatriculaNotas`. Utilización de la nueva clase de asociación.

```java

public interface MatriculaNotasRepository extends JpaRepository<MatriculaNotas, NotasPK>{

}
```

En el caso del servicio, hemos aprovechado para añadir algo más de implementación, con la idea del [ejemplo 10](https://github.com/lmlopezmagana/bbdd-2020/tree/master/10_ManyToManyBidiLogicaNegocio), para añadir algo de lógica de negocio: 

- La matriculación de un alumno en todas las asignaturas de un curso.
- Poner la nota trimestral de un alumno en una asignatura.

> ¡OJO! Por facilitaros un poco el ejemplo, todas las asociaciones son EAGER. Esto no sería real en una aplicación en producción. No todas las asociaciones se van a mantener de forma _ansiosa_. Tendríais que trabajar, asociación a asociación, la forma de mantener ese enlace (_lazy_ o _eager_).

```java
Service
public class MatriculaNotasServicio extends BaseService<MatriculaNotas, NotasPK, MatriculaNotasRepository> {

	private final AlumnoServicio alumnoServicio;

	public MatriculaNotasServicio(MatriculaNotasRepository repo, AlumnoServicio alumnoServicio) {
		super(repo);
		this.alumnoServicio = alumnoServicio;
	}

	// Método que matricula a un alumno en todas las asignaturas de un curso
	public Alumno matricularAlumno(Alumno alumno, Curso curso) {
		curso.addAlumno(alumno);
		alumnoServicio.save(alumno);
		for (Asignatura asignatura : curso.getAsignaturas()) {
			MatriculaNotas m = new MatriculaNotas(alumno, asignatura);
			this.save(m);
		}

		// Ejecutamos la consulta para "refrescar" el alumno
		// con sus asociaciones, de manera que las colecciones
		// de tipo EAGER vendrán rellenas de datos sin
		// necesidad de métodos helper.
		return alumnoServicio.findById(alumno.getId());

	}

	// Método para poner la nota de un trimestre
	// En caso de no esté matriculado en la asignatura o no exista el trimestre
	// devolvemos null
	public Alumno notaTrimestral(Alumno alumno, Asignatura asignatura, int trimestre, int nota) {

		NotasPK pk = new NotasPK();
		pk.setAlumno_id(alumno.getId());
		pk.setAsignatura_id(asignatura.getId());
		MatriculaNotas m = this.findById(pk);
		if (m != null) {
			boolean modificado = false;
			switch (trimestre) {
			case 1:
				m.setPrimeraEv(nota);
				modificado = true;
				break;
			case 2:
				m.setSegundaEv(nota);
				modificado = true;
				break;
			case 3:
				m.setTerceraEv(nota);
				modificado = true;
				break;
			case 4:
				m.setNotaFinal(nota);
				modificado = true;
				break;
			default:
				return null;
			}
			if (modificado) {
				this.edit(m);
				return alumnoServicio.findById(alumno.getId());
			}
		}

		return null;

	}

}
```

### Paso 5: Tratamiento bidireccional y no olvidar evitar referencias circulares

Como hemos comprobado, hemos pasado **de una asociación `@ManyToMany` bidireccional** a **dos asociaciones `@ManyToOne` + `@OneToMany`**. No hemos implementado el tratamiento bidireccional a través de métodos _helpers_ por no estimarlo conveniente para este ejemplo. Quedaría **a elección del usuario el poder hacerlo**.

Sí que necesitaríamos excluir las listas en los métodos `equals`, `hashCode` y `toString` para evitar referencias circulares.
