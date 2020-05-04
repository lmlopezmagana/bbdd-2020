
# Ejemplo 7 - Ejemplo una asociación _One To Many_ (versión 1)

## Asociaciones _uno-a-muchos_

Normalmente, cuando se estudia UML, se suelen aprender las asociaciones y su multiplicidad (en ocasiones, conocida como _cardinalidad_ ) sin tener en cuenta la dirección. Por ejemplo, en el siguiente
diagrama:

![Diagrama](./uml.jpg)

La asociación `está-en` entre las clases `Alumno` y `Curso` se suele decir que es una asociación _uno a muchos_. Sin embargo, viendo el ejemplo [anterior](https://github.com/lmlopezmagana/bbdd-2020/tree/master/06_ManyToOne) hemos comprobado que depende de la forma en que leamos la asociación, será de un tipo u otro:

- Si la leemos desde `Alumno` a `Curso`, tendremos una asociación _muchos-a-uno_.
- Si la leemos desde `Curso` a `Alumno`, tendremos una asociación _uno-a-muchos_.

**¿Qué alternativas de tratamiento tenemos en estos casos?** Ciertamente, JPA nos ofrece varias formas de implementar una asociación uno a muchos. Cada una con sus ventajas y desventajas:

| **Estrategia** 	| Ventajas 	| Desventajas 	|
|------------	|----------	|-------------	|
| Implementar solo la asociación _muchos-a-uno_ (solo utilizados `@ManyToOne`) | Implementación fácil. Basta incluir la anotación `@ManyToOne`en el lado muchos, y JPA crea todo lo necesario por nosotros. 	| En el lado uno, no tenemos conocimiento de las instancias del lado muchos con la cuál está asociada 	|
| Implementar solo la asociación _uno-a-muchos_ (solo usamos `OneToMany`) | Implementación fácil. Tan solo usamos la anotación `@OneToMany` en el lado uno. Esta anotación va asociada a una colección (de tipo `List` o `Set`| Muy bajo rendimiento. Se genera una tabla intermedia, y se realizan múltiples operaciones para inserción, actualización y consulta. 	|
| **Implementación _bidireccional_: se utiliza la anotación `@ManyToOne` en el lado muchos, y `@OneToMany` en el lado uno.**  | Toda la asociación se maneja a través de una _clave externa_, con lo que ganamos en rendimiento. | Implementación más compleja. Requiere de métodos auxiliares que mantengan el estado de la asociación en ambos extremos. 	|
| **Implementar solo la asociación _muchos-a-uno_ y utilizar consultas para obtener la información del lado _uno-a-muchos_** | Fácil implementación de entidades. Solo _penalizamos_ el rendimiento del sistema en el momento de consultar. Muy útil para aquellos que saben SQL/JPQL | Implementación de consultas más compleja.

Como podemos ver en el énfasis del texto, por cuestiones de rendimiento las mejores opciones son las dos últimas. En este ejemplo veremos la **implementación bidireccional**.

## Paso 1: Implementación de la asociación en el lado _muchos_

Ya la tenemos del ejemplo anterior:

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
	
	
	// Resto del código	
}
```

## Paso 2: Implementación de la asociación en el lado _uno_

### Algo de teoría

Como hemos visto antes, la anotación que ofrece JPA para las asociaciones _uno-a-muchos_ es `@OneToMany`. Dicha anotación debe ir asociada a una colección. Si revisamos la documentación o la literatura al respecto, encontramos:

- En el caso de las asociaciones `@OneToMany` unidireccionales, recomiendan el uso de `Set`.
- En el caso de las asociaciones `@OneToMany` bidireccionales, podemos utilizar `List` sin ver penalizado el rendimiento.

Por tanto, tendríamos que tener algo parecido a esto:

```java
@OneToMany
private List<Alumno> alumnos;
```

Sin embargo, con esta información no basta. **Como queremos darle a nuestra asociación un tratamiento bidireccional**, necesitamos indicar que esta asociación _uno-a-muchos_ viene _mapeada_ por una asociación _muchos-a-uno_. La forma de hacerlo es mediante el atributo `mappedBy` de la propia anotación. **Tenemos que indicar el nombre del atributo de la clase opuesta (en nuestro caso, `Alumno`, que está anotado con `@ManyToOne`)**.

`@ManyToOne`
```java
@ManyToOne
private Curso curso;
``` 

`@OneToMany`
```java
@OneToMany(mappedBy="curso")
private List<Alumno> alumnos;
```

### Métodos auxiliares para mantener ambos extremos de la asociación

El tratamiento bidireccional de la asociación no es gratuito. Si queremos dar a una asociación un tratamiento bidireccional, tendremos que **asignar la asociación en ambos extremos**. Para facilitar la operación, se suelen crear unos métodos auxiliares (en ocasiones conocidos como _helpers_) que realicen esta doble asignación.

**¿Dónde se implementan estos métodos?** Deben estar en una de las dos clases modelo. Normalmente, suelen estar en el lado uno (en nuestro ejemplo, los dejaremos en `Curso`).

```java
@Entity
public class Curso {
	
	// Resto del código
	
	@OneToMany(mappedBy="curso")
	private List<Alumno> alumnos;
	
	// Resto del código	
	
	public void addAlumno(Alumno a) {
		this.alumnos.add(a);
		a.setCurso(this);
	}
	
	public void removeAlumno(Alumno a) {
		this.alumnos.remove(a);
		a.setCurso(null);
	}
}
```

## Paso 3: Cómo trabajar con ahora con `Alumno` y `Curso`

Para trabajar ahora con nuestras entidades asociadas, tendremos que usar los métodos que hemos definido en el paso anterior.

```java
			// Creamos un nuevo curso y lo almacenamos
			Curso nuevoCurso = new Curso("1º DAM", "Miguel Campos");
			cursoServicio.save(nuevoCurso);

			// Creamos un nuevo conjunto de alumnos
			List<Alumno> nuevos = Arrays.asList(new Alumno("Antonio", "Pérez", "antonio.perez@gmail.com"),
					new Alumno("María", "López", "maria.lopez@gmail.com"));


			for(Alumno a : nuevos )
				alumnoServicio.save(a);
			
			// Aquí es donde vinculamos el curso y los alumnos.
			// La entidad Curso es la que tiene los metodos auxiliares
			// aunque en realidad, Alumno es el propietario de la asociación
			
		
			for (Alumno a : nuevos) {
				nuevoCurso.addAlumno(a);
				alumnoServicio.edit(a);
			}

```

Como podemos comprobar, para asociar un curso a una serie de alumnos, tenemos que usar el método helper correspondiente, y (en este caso) almacenar el lado _muchos_, que es quién realmente almacena a nivel de base de datos la asociación (la clave externa).

## Paso 4: ¡IMPORTANTE! Hay que evitar las referencias circulares

Como decíamos antes, el tratamiento bidireccional de una asociación **no es gratuito**. Otro problema que se nos presenta es el de los métodos `equals`, `hashCode` y `toString`. 

> Antes de nada, os recomiendo leer un artículo de Vlad Mihalcea, que es uno de los referentes que podemos encontrar por internet sobre JPA, Hibernate y Spring. Se trata de [https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/](https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/); este artículo nos ayuda a reflexionar sobre la mejor manera de implementar estos métodos cuando trabajamos con JPA e Hibernate. Si utilizamos la aproximación de Vlad Mihalcea, nos evitaríamos el problema que desarrollo en este apartado.

El problema que se nos presenta aquí es de como se implementan estos métodos. Supongamos el método `toString` de la clase `Curso`. _Grosso modo_, este método realiza una operación parecida a la siguiente:

```
resultado <- ""
Para toda propiedad p de la clase Curso
	resultado <- resultado + p.toString() 
devolver resultado
```

El problema lo tenemos cuando llega a tratar la propiedad `alumno`, ya que al llamar al método `toString` de este, y ser una lista, el tratamiento que se hace es invocar a `toString` de cada una de las instancias que contiene. A su vez, cada instancia de `Alumno` tiene una propiedad llamada `curso`. De hecho, ¡es el mismo curso sobre el que ya hemos invocado `toString`!. 

Como podemos ver, tenemos una **referencia circular**, que nos va a provocar **un desbordamiento de la pila.**

Una forma de solucionarlo es excluir de estos métodos (`equals`, `hashCode` y `toString`) la colección anotada con `@OneToMany`. _Lombok_ nos permite hacer esto fácilmente a través de las anotaciones `@EqualsAndHashcode.Exclude` y `@ToString.Exclude` sobre el atributo en cuestión.

```java
@EqualsAndHashCode.Exclude
@ToString.Exclude
@OneToMany(mappedBy="curso")
private List<Alumno> alumnos = new ArrayList<>();
```

## Y, ¿cuál sería la otra estrategia de implementación de _uno-a-muchos_?

Como indicábamos en la tabla inicial, sería a través del uso de consultas. La estudiaremos cuando aprendamos a manejarlas.