package com.salesianostriana.dam;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.modelo.Alumno;
import com.salesianostriana.dam.modelo.Curso;
import com.salesianostriana.dam.servicios.AlumnoServicio;
import com.salesianostriana.dam.servicios.CursoServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner init(AlumnoServicio alumnoServicio, CursoServicio cursoServicio) {
		return args -> {

			System.out.println("ALUMNOS que no están matriculados en ningún curso");
			System.out.println("===================================");
			List<Alumno> alumnosSinCurso = alumnoServicio.alumnosSinCurso();
			if (alumnosSinCurso != null) {
				System.out.println("Tamaño: " + alumnosSinCurso.size());
			}
			// Con este fragmento de código hemos comprobado que no hay ningún alumno sin curso
			// por lo que la consulta nos devuelve una lista no nula, pero vacía.
			System.out.println("\n\n");

			System.out.println("ALUMNOS de 1º DAM que han nacido después de 01/01/1995");
			System.out.println("===================================");
			List<Alumno> alumnosDe1995EnAdelante = alumnoServicio.alumnosFechaNacimientoDespuesDe(LocalDate.of(1995, 1, 1), "1º DAM");
			alumnosDe1995EnAdelante.forEach(a -> {
				System.out.println(a.getNombre() + " " + a.getApellido1() + " " + a.getApellido2() + " " + a.getFechaNacimiento());
			});
			System.out.println("\n\n");

			
			
			System.out.println("CURSO 1 con su listado de alumnos");
			System.out.println("===================================");
			Curso c = cursoServicio.cursoConAlumnosPorId(1l);
			System.out.println(c.getNombre());
			c.getAlumnos().forEach(System.out::println);
			System.out.println("\n\n");
			
		};
	}

}
