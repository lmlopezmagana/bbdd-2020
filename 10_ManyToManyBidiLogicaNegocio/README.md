# Ejemplo 10 - Algo de lógica de negocio en con una asociación _many-to-many_

Partimos desde el ejemplo [anterior](https://github.com/lmlopezmagana/bbdd-2020/tree/master/09_ManyToManyBidi)

## ¿Cuál es nuestro modelo de datos?

Sigue siendo el mismo

![diagrama uml](./uml.jpg) 

## Lógica de negocio

Entonces, ¿qué es lo que estamos añadiendo? En muchas ocasiones el **concepto de lógica de negocio**. Pero, ¿qué es eso? 

Según la wikipedia:

> Las Reglas del Negocio o Conjunto de Reglas de Negocio describe las políticas, normas, operaciones, definiciones y restricciones presentes en una organización y que son de vital importancia para alcanzar los objetivos misionales.

Es decir, se trata de la implementación de aquellos procesos, flujos de información, reglas de validación, ... propias del sistema que estamos implementando, y que no tienen por qué ser las mismas en otro sistema.

Es decir, que por un lado tendríamos las operaciones de gestión de cada entidad (insertar, eliminar, actualizar, consultar), y por otro lado, tendríamos la lógica de negocio. Esta última puede _utilizar varios CRUD para realizar su misión_.


## Ejemplo

Continuemos con nuestro ejemplo de un colegio. Uno de los procesos propios que definen un colegio es la matriculación de los alumnos. Esto, por ejemplo, podría incluir los siguientes pasos:

1. Insertar al alumno.
2. Asociarlo al curso al que se matricula.
3. Asociarlo a todas las asignaturas de ese curso (matrícula completa)
4. Enviar un email de confirmación.

La implementación de estos pasos en **uno o más servicios** sería la lógica de negocio de nuestra aplicación.

### Código de envío de email

Para no _enmarañar_ el ejemplo con código real, usamos un servicio _dummy_, que realmente va a escribir el contenido del mensaje por el log:

```java
@Service
public class MailService {

	
	private final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	// Este método sirve para simular el envío de un email.
	// En lugar de enviarlo, imprime todo por consola.
	public void send(String to, String subject, String body) {
		logger.info("Destinatario : " + to);
		logger.info("Asunto: " + subject);
		logger.info("Mensaje : " + body);
	}
	
	
}
```

### Código de matrícula

Este sería el servicio que se encargaría de implementar los pasos descritos anteriormente. Como podemos comprobar es un servicio que no hereda del servicio base: no sirve para hacer las operaciones CRUD de una entidad, sino para implementar la lógica de la matriculación de alumnos.

```java
@Service
@RequiredArgsConstructor
public class MatriculaServicio {
	
	private final AlumnoServicio alumnoServicio;
	private final MailService mailService;
	
	public Alumno matriculaAlumnoEnTodasAsignaturasDeUnCurso(Alumno a, Curso c) {
		
		if (a != null && c != null) {
			// Está el alumno almacenado? Y el curso? Si no, lo guardamos
			if (a.getId() < 1) {
				alumnoServicio.save(a);
			}
						
			// Añadimos el alumno al curso.
			c.addAlumno(a);
			
			// Almacenamos en la base de datos
			alumnoServicio.edit(a);
			
			// Obtenemos todas las asignaturas del curso y matriculamos al alumno
			for (Asignatura asignatura : c.getAsignaturas()) {
				a.addAsignatura(asignatura);
			}
			// "Matriculamos al alumno en las asignaturas guardándolo en la base de datos."
			alumnoServicio.edit(a);
			
			/*
			 * Las líneas anteriores se podrían ajustar y resumir un poco
			 *
			 */
//			c = cursoServicio.findByIdEager(c.getId());
//			c.addAlumno(a);
//			for (Asignatura asignatura : c.getAsignaturas()) {
//				a.addAsignatura(asignatura);
//			}
//			alumnoServicio.edit(a);
			
			// Enviamos el email para confirmar la matrícula
			StringBuilder cuerpo = new StringBuilder();
			cuerpo.append("El alumno " + a.getNombre() + " " + a.getApellidos());
			cuerpo.append(" se ha matriculado del curso " + c.getNombre());
			cuerpo.append(" y de las siguientes asignaturas: ");
			cuerpo.append(
					a.getAsignaturas().stream()
						.map(Asignatura::getNombre)
						.collect(Collectors.joining(", "))	
					);
			cuerpo.append(" Un saludo");
			mailService.send(a.getEmail(), "Confirmación de matrícula", cuerpo.toString());
				
			return a;
			
		} else
			return null;
		
		
	}
	
```

## Ejemplo de uso

```java
// Ejemplo de matrícula
System.out.println("Ejemplo de algo de lógica de negocio");
System.out.println("=====================");
Alumno nuevo = new Alumno("Pepe", "Pérez", "pepe@pepe.com");
			
matriculaServicio.matriculaAlumnoEnTodasAsignaturasDeUnCurso(nuevo, primeroDam);

```

