package com.salesianostriana.dam.primerproyectodatajpa;

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
			
			// A través de AlumnoServicio podemos gestionar Alumnos
			alumnoServicio.save(new Alumno("Luis Miguel","López Magaña","luismi.lopez@salesianos.edu"));
			alumnoServicio.save(new Alumno("Ángel","Naranjo González","angel.naranjo@salesianos.edu"));
			alumnoServicio.save(new Alumno("Rafael", "Villar Liñán", "rafael.villar@salesianos.edu"));
			
			List<Alumno> listaAlumnos = alumnoServicio.findAll();
			System.out.println("\n--- alumnoServicio.findAll() ---");
			for( Alumno a : listaAlumnos) {
				System.out.println(a);
			}
			System.out.println("\n");
			
			
			
			Curso primeroDam = new Curso("1º DAM", "Miguel Campos");
			
			cursoServicio.save(primeroDam);
			
			
			for(Alumno a: listaAlumnos) {
				a.setCurso(primeroDam);
				alumnoServicio.edit(a);
			}
			
			System.out.println("\n--- alumnoServicio.findAll() ---");
			listaAlumnos = alumnoServicio.findAll();
			for( Alumno a : listaAlumnos) {
				System.out.println(a);
			}
			System.out.println("\n");
			
			Curso segundoDam = new Curso("2º DAM", "Luismi López");
			
			cursoServicio.save(segundoDam);
			
			System.out.println("\n--- cursoServicio.findAll() ---");
			List<Curso> listaCursos = cursoServicio.findAll();
			for(Curso c : listaCursos) {
				System.out.println(c);
			}
			
			
			
		};
	}

}
