package com.salesianostriana.dam.primerproyectodatajpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.AlumnoRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(AlumnoRepository repo) {
		return args -> {
			
			repo.save(new Alumno("Luis Miguel","López Magaña","luismi.lopez@salesianos.edu"));
			repo.save(new Alumno("Ángel","Naranjo González","angel.naranjo@salesianos.edu"));
			repo.save(new Alumno("Rafael", "Villar Liñán", "rafael.villar@salesianos.edu"));
			
			repo.findAll().forEach(System.out::println);
			
		};
	}

}
