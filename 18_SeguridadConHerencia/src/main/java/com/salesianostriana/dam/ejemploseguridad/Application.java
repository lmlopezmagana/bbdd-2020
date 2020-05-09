package com.salesianostriana.dam.ejemploseguridad;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.salesianostriana.dam.ejemploseguridad.seguridad.modelo.Cliente;
import com.salesianostriana.dam.ejemploseguridad.seguridad.modelo.Empleado;
import com.salesianostriana.dam.ejemploseguridad.servicios.UsuarioServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(UsuarioServicio servicio, PasswordEncoder passwordEncoder) {
		return args -> {
			
			Empleado emple = new Empleado();
			emple.setEmail("luismi.lopez@email.com");
			emple.setPassword(passwordEncoder.encode("1234"));
			emple.setFechaAltaSS(LocalDate.of(2019, 1, 1));
			
			servicio.save(emple);
			
			Cliente cliente = new Cliente();
			cliente.setEmail("angel.naranjo@email.com");
			cliente.setPassword(passwordEncoder.encode("1234"));
			cliente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
			
			servicio.save(cliente);
			
			
		};
	}

}
