package com.salesianostriana.dam.primerproyectodatajpa;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.AlumnoServicio;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.CursoServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(AlumnoServicio alumnoServicio, CursoServicio cursoServicio) {
		return args -> {

			
			// Creamos un nuevo curso y lo almacenamos
			Curso nuevoCurso = new Curso("1º DAM", "Miguel Campos");
			cursoServicio.save(nuevoCurso);

			// Creamos un nuevo conjunto de alumnos
			List<Alumno> nuevos = Arrays.asList(new Alumno("Antonio", "Pérez", "antonio.perez@gmail.com"),
					new Alumno("María", "López", "maria.lopez@gmail.com"));


			for(Alumno a : nuevos )
				alumnoServicio.save(a);
			
			// También creamos otro alumno fuera de la colección			
			Alumno otro = new Alumno("Manuel", "Sánchez Sánchez", "manuesanchesanche@sellamaba.el");
			alumnoServicio.save(otro);
			
			// Así se podría hacer con lambda
			// nuevos.forEach(alumnoServicio::save);

			
			// Aquí es donde vinculamos el curso y los alumnos.
			// La entidad Curso es la que tiene los metodos auxiliares
			// aunque en realidad, Alumno es el propietario de la asociación
			
		
			for (Alumno a : nuevos) {
				nuevoCurso.addAlumno(a);
				alumnoServicio.edit(a);
			}

			// Así se podría hacer con lambda
			/*
			nuevos.forEach((a) -> {
				nuevoCurso.addAlumno(a);
				alumnoServicio.edit(a)
			});
			*/
			

			// Estos son todos los alumnos asignados a este curso
			// Podemos ver que el alumno Manuel Sanchez no aparece,
			// porque no está asignado a este curso.
			for (Alumno a : nuevoCurso.getAlumnos()) {
				System.out.println(a);
			}
			
			
			
			// Así se podría hacer con lambda			
			// nuevoCurso.getAlumnos().forEach(System.out::println);

			
			System.out.println(nuevoCurso);
			System.out.println(nuevoCurso.getAlumnos());
			
			
			List<Alumno> alumnosAlmacenados = alumnoServicio.findAll();
			
			Curso cursoAlmacenado = cursoServicio.findById(1L);
			
			System.out.println("\n\nDatos almacenados");
			
			System.out.println(cursoAlmacenado);
			//System.out.println(cursoAlmacenado.getAlumnos());
			
			alumnosAlmacenados.forEach(System.out::println);
			
		};
	}

}
