package com.salesianostriana.dam.e01ejerciciodao;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.e01ejerciciodao.modelo.Alumno;
import com.salesianostriana.dam.e01ejerciciodao.servicios.AlumnoServicio;

@SpringBootApplication
public class E01EjercicioDaoSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(E01EjercicioDaoSpringBootApplication.class, args);
	}
	
	
	
	
	@Bean
	public CommandLineRunner app(AlumnoServicio servicio) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				
				// Insertar algunos alumnos para poder probar.
				servicio.insert(new Alumno(servicio.makeID(), "Luis Miguel", "López Magaña", "luismi.lopez@salesianos.edu",
						LocalDate.of(1982, 9, 18)));
				servicio.insert(new Alumno(servicio.makeID(), "Miguel", "Campos Rivera", "miguel.campos@salesianos.edu",
						LocalDate.of(1984, 1, 1)));
				servicio.insert(new Alumno(servicio.makeID(), "Ángel", "Naranjo González", "angel.naranjo@salesianos.edu",
						LocalDate.of(1976, 12, 1)));
				Alumno rafa = new Alumno(servicio.makeID(), "Rafael", "Villar Liñán", "rafa.villar@salesianos.edu",
						LocalDate.of(1980, 6, 15));
				servicio.insert(rafa);
				
				
				
				// Obtener todos los alumnos e imprimirlos por consola
				System.out.println("TODOS LOS ALUMNOS");
				for(Alumno a : servicio.findAll()) {
					System.out.println(a);
				}
				
				// Obtenemos los alumnos, ordenados por edad
				System.out.println("\n\nALUMNOS ORDENADOS POR EDAD");
				for(Alumno a : servicio.getByEdad()) {
					System.out.println(a);
				}
				
				
				// Buscamos y editamos un alumno
				Alumno buscado = servicio.findById(rafa.getId());
				buscado.setEmail("rafael.villar@salesianos.edu");
				servicio.edit(buscado);
				
				
				// Obtenemos todos los alumnos, ordenados alfabéticamente
				System.out.println("\nALUMNOS ORDENADOS ALFABÉTICAMENTE");
				// Lo hacemos al estilo lambda
				servicio.getByOrdenAlfabetico().forEach(System.out::println);
				
			}
			
		};
	}
	

}
