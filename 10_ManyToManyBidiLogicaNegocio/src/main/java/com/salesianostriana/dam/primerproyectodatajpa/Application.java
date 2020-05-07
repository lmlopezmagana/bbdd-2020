package com.salesianostriana.dam.primerproyectodatajpa;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Asignatura;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.AlumnoServicio;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.AsignaturaServicio;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.CursoServicio;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.MatriculaServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(AlumnoServicio alumnoServicio, 
			CursoServicio cursoServicio, 
			AsignaturaServicio asignaturaServicio, 
			MatriculaServicio matriculaServicio) {
		return args -> {

			
			// Creamos un nuevo curso y lo almacenamos
			Curso primeroDam = new Curso("1º DAM", "Miguel Campos");
			cursoServicio.save(primeroDam);

			// Creamos las asignaturas, las guardamos y las añadimos al curso
			List<Asignatura> asignaturas = Arrays.asList(
					new Asignatura("Bases de Datos", "Luismi"),
					new Asignatura("Entornos de Desarrollo", "Luismi"),
					new Asignatura("Programación", "Ángel"),
					new Asignatura("Lenguajes de Marcas", "Rafa")
					);
			
			
			for (Asignatura a : asignaturas) {
				primeroDam.addAsignatura(a);
				asignaturaServicio.save(a);
			}
			
			
			
			
			// Creamos un nuevo conjunto de alumnos
			List<Alumno> nuevos = Arrays.asList(new Alumno("Antonio", "Pérez", "antonio.perez@gmail.com"),
					new Alumno("María", "López", "maria.lopez@gmail.com"));


			for(Alumno a : nuevos)
				alumnoServicio.save(a);
			
			
			// Aquí es donde vinculamos el curso y los alumnos.
			// La entidad Curso es la que tiene los metodos auxiliares
			// aunque en realidad, Alumno es el propietario de la asociación
			
		
			for (Alumno a : nuevos) {
				primeroDam.addAlumno(a);
				alumnoServicio.edit(a);
			}
			
			
			
			// Ejemplo de matrícula
			System.out.println("Ejemplo de algo de lógica de negocio");
			System.out.println("=====================");
			Alumno nuevo = new Alumno("Pepe", "Pérez", "pepe@pepe.com");
			
			matriculaServicio.matriculaAlumnoEnTodasAsignaturasDeUnCurso(nuevo, primeroDam);

			System.out.println(nuevo);
			
		};
	}

}
