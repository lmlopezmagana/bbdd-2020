
# Ejemplo 9 - Ejemplo una asociación _Many To Many_ (bidireccional)

Partimos desde el ejemplo [one-to-many](https://github.com/lmlopezmagana/bbdd-2020/tree/master/07_OneToManyBidi)

## ¿Cuál es nuestro modelo de datos?

![diagrama uml](./uml.jpg) 

## Algo de teoría sobre asociaciones _ManyToMany_

Una asociación _muchos-a-muchos_ es un tipo de _relación_ entre clases, por la cual, una instancia de uno de los tipos conectados, se puede asociar a muchas instancias del otro tipo, y viceversa. Uno de los ejemplos clásicos suele ser el de las clases `Autor` y `Libro`, mediante la cual un `Autor` puede escribir muchos `Libro`, y un `Libro` puede ser escrito por muchos autores. 

Aunque esta asociación tenga la misma _multiplicidad_ en ambos extremos, también puede tratarse de forma bien **unidireccional**, bien **bidireccional**. Escoger una u otra queda a cargo del programador.

JPA nos ofrece una anotación parecida a las anteriores, `@ManyToMany`, que nos permitirá manejar la relación. En este caso, dicha anotación irá asociada a una colección (normalmente, un `Set` o un `List`).

```java
@ManyToMany
List<Entity> myList = new ArrayList<>();
```

A nivel de DDL, se crea una nueva tabla, con _claves externas_ hacia las tablas de las entdidades asociadas. 

### Anotaciones adicionales

Además de la anotación `@ManyToMany`, podemos completar esta asociación con algo de metainformación, a través de las siguientes anotaciones: 

- `@JoinTable`: nos permite dar algo de configuración a la tabla _join_ que se genera.
	- `name`: podemos cambiar el nombre por defecto de esta tabla
	- `joinColumns`: nos permite proporcionar una colección indicando los `@JoinColumn` (y esto a su vez, nos permite indicar todas las propiedades de cada `@JoinColumn`, como el `name` , `columnDefinition`, ...
	- `inverseJoinColumns`: nos permite proporcionar una colección de claves externas (similar al anterior) pero que apuntan a la entidad que no posee la asociación _muchos-a-muchos_.

## Tratamiento bidireccional de una asociación `@ManyToMany`

El tratamiento _bidireccional_ de una asociación nos permite utilizar dicha asociación desde las dos entidades que están conectadas. Partimos del código del ejemplo anterior, que hemos modificado un poco:

```java
@Data @NoArgsConstructor
@AllArgsConstructor
@Entity
public class Curso {

	@Id @GeneratedValue
	private long id;
	
	private String nombre;
	private String tutor;
	
	
	public Curso(String nombre, String tutor) {
		this.nombre = nombre;
		this.tutor = tutor;
	}
	
	/** ASOCIACIÓN ONE TO MANY CON ALUMNO **/
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="curso")
	private List<Alumno> alumnos = new ArrayList<>();
	
	
	/********************************************/
	/* MÉTODOS AUXILIARES					    */
	/********************************************/
	
	/**
	 * Método auxiliar para el tratamiento bidireccional de la asociación. Añade a un alumno
	 * a la colección de alumnos de un curso, y asigna a dicho alumno este curso como el suyo.
	 * @param a
	 */
	public void addAlumno(Alumno a) {
		this.alumnos.add(a);
		a.setCurso(this);
	}
	
	/**
	 * Método auxiliar para el tratamiento bidireccional de la asociación. Elimina un alumno
	 * de la colección de alumnos de un curdso, y desasigna a dicho alumno el curso, dejándolo como nulo.
	 * @param a
	 */
	public void removeAlumno(Alumno a) {
		this.alumnos.remove(a);
		a.setCurso(null);
	}
	
	
	
	/** ASOCIACIÓN ONE TO MANY CON ASIGNATURAS **/
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="curso")
	private List<Asignatura> asignaturas = new ArrayList<>();
	
	
	// Métodos helper
	public void addAsignatura(Asignatura a) {
		this.asignaturas.add(a);
		a.setCurso(this);
	}
	
	public void removeAsignatura(Asignatura a) {
		this.asignaturas.remove(a);
		a.setCurso(null);
	}
}



@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String nombre, apellidos, email;
	
	@ManyToOne
	private Curso curso;
	
	public Alumno(String n, String a, String e) {
		this.nombre = n;
		this.apellidos = a;
		this.email = e;
	}

	public Alumno(String nombre, String apellidos, String email, Curso curso) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.curso = curso;
	}
	
	

}


public class Asignatura {
	
	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private String profesor;
	
	@ManyToOne
	private Curso curso;
	
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


## Paso 1: Modificar la entidad `Alumno`

```java
@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	// Resto del código
	
	
	@ManyToMany
	@JoinTable(
		joinColumns = @JoinColumn(name="alumno_id"),
		inverseJoinColumns = @JoinColumn(name="asignatura_id")
	)
	private List<Asignatura> asignaturas = new ArrayList<>();
	

	/** MÉTODOS HELPERS **/
	
	public void addAsignatura(Asignatura a) {
		asignaturas.add(a);
		a.getAlumnos().add(this);
	}
	
	public void removeAsignatura(Asignatura a) {
		asignaturas.remove(a);
		a.getAlumnos().remove(this);
	}
	

}
```

## Paso 2: Modificar la entidad `Asignatura`

```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Asignatura {
	
	// Resto del código

	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy="asignaturas")
	private List<Alumno> alumnos = new ArrayList<>();
	
	// Resto del código	

}
```

Si ejecutamos el proyecto, podemos apreciar el siguiente DDL generado:

```sql
   create table alumno (
       id bigint not null,
        apellidos varchar(255),
        email varchar(255),
        nombre varchar(255),
        curso_id bigint,
        primary key (id)
    )

    
    create table alumno_asignaturas (
       alumno_id bigint not null,
        asignatura_id bigint not null
    )

    
    create table asignatura (
       id bigint not null,
        nombre varchar(255),
        profesor varchar(255),
        curso_id bigint,
        primary key (id)
    )

    
    create table curso (
       id bigint not null,
        nombre varchar(255),
        tutor varchar(255),
        primary key (id)
    )

    
    alter table alumno 
       add constraint FKojks48ahsqwkx9o2s7pl0212p 
       foreign key (curso_id) 
       references curso

    
    alter table alumno_asignaturas 
       add constraint FKrukf4f8fa1cexj0cwh1m7kpim 
       foreign key (asignatura_id) 
       references asignatura

    
    alter table alumno_asignaturas 
       add constraint FKrt0id7bs1lp5qkuyhw9ceustn 
       foreign key (alumno_id) 
       references alumno

    
    alter table asignatura 
       add constraint FKr7icgav26emducg973dp80fga 
       foreign key (curso_id) 
       references curso
```

## Paso 3: Utilizar esta asociación

En este caso, siendo bidireccional, la tenemos que manejar a través de los métodos helpers.