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
import com.salesianostriana.dam.primerproyectodatajpa.servicios.MatriculaNotasServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(AlumnoServicio alumnoServicio, 
			CursoServicio cursoServicio, 
			AsignaturaServicio asignaturaServicio, 
			MatriculaNotasServicio matriculaNotasServicio) {
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
			
			
			
			
			Alumno otro = new Alumno("Pepe", "Pérez", "pepe@perez.com");
			alumnoServicio.save(otro);
			
			Alumno yaMatriculado = matriculaNotasServicio.matricularAlumno(otro, primeroDam);
			System.out.println(yaMatriculado);
			System.out.println(yaMatriculado.getMatriculaNotas());
			
			// Le vamos a poner nota en base de datos.
			
			
			// Esta búsqueda no se debe hacer así, es solo de ejemplo
			Asignatura basedatos = asignaturas.get(0);
			Alumno conNota = matriculaNotasServicio.notaTrimestral(yaMatriculado, basedatos, 3, 10);
			
			
			System.out.println(conNota);
			System.out.println(conNota.getMatriculaNotas());
			
		};
	}

}
