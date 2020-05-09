package com.salesianostriana.dam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.modelo.Bebida;
import com.salesianostriana.dam.modelo.Comida;
import com.salesianostriana.dam.servicios.BebidaServicio;
import com.salesianostriana.dam.servicios.ComidaServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(BebidaServicio bebidaServicio, ComidaServicio comidaServicio) {
		return args -> {
			
			Bebida botellin = new Bebida("Botellín de Cruzcampo", 1.0f, 25);			
			bebidaServicio.save(botellin);
			bebidaServicio.findAll().forEach(System.out::println);
			
			Comida tapaEnsaladilla = new Comida("Tapa de ensaladilla", 2.5f, true, "Entrante");
			comidaServicio.save(tapaEnsaladilla);
			comidaServicio.findAll().forEach(System.out::println);
			
			
		};
	}
	
	

}
