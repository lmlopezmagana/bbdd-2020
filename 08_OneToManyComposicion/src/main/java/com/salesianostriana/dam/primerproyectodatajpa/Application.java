package com.salesianostriana.dam.primerproyectodatajpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Componente;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Compuesto;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.CompuestoServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(CompuestoServicio servicio) {
		return args -> {

			// Al ser una composición, y los componentes no
			// tener "entidad suficiente" (entidades débiles)
			// todas las operaciones se realizan a través
			// de la entidad fuerte (Compuesto)
			
			Compuesto compuesto = new Compuesto("Valor 1", "Valor 2", LocalDate.now());
			
			// Podemos construir los compuestos directamente al añadir
			compuesto.addComponente(new Componente("v1", 2, 3.0));
			
			// También podemos construirlos fuera, y añadir la referencia
			Componente componente2 = new Componente("v2", 4, 6.0);
			compuesto.addComponente(componente2);

			// Miremos qué sucede en el log al almacenar el componente
			servicio.save(compuesto);
			
			// Añadimos otro compuesto con sus componentes
			Compuesto otroCompuesto = new Compuesto("Cadena 1", "Cadena 2", LocalDate.of(1990, 1, 1));
			otroCompuesto.addComponente(new Componente("One", 2, 3.0));
			otroCompuesto.addComponente(new Componente("Cuatro", 5, 6.0));
			otroCompuesto.addComponente(new Componente("Siete", 8, 9.0));
			servicio.save(otroCompuesto);
			
			
			// ¿Qué consulta SQL se ejecuta al obtener la lista de todos los compuestos?
			List<Compuesto> result = servicio.findAll();
			
			System.out.println("\n");
			for(Compuesto c : result) {
				System.out.println("Compuesto: " + c);
				System.out.println("Componentes: " + c.getComponentes());
			}
			
			
						
		};
	}

}
