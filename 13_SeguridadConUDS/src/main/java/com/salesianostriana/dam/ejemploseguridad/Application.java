package com.salesianostriana.dam.ejemploseguridad;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.salesianostriana.dam.ejemploseguridad.seguridad.modelo.Usuario;
import com.salesianostriana.dam.ejemploseguridad.servicios.UsuarioServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(UsuarioServicio servicio, PasswordEncoder passwordEncoder) {
		return args -> {
			
			Usuario u = new Usuario();
			u.setAdmin(false);
			u.setNombre("Luis Miguel");
			u.setApellidos("López");
			u.setEmail("luismi.lopez@email.com");
			u.setPassword(passwordEncoder.encode("1234"));
			
			servicio.save(u);
			
			
			Usuario a = new Usuario();
			a.setAdmin(true);
			a.setNombre("Ángel");
			a.setApellidos("Narajo");
			a.setEmail("angel.naranjo@email.com");
			a.setPassword(passwordEncoder.encode("1234"));
			
			servicio.save(a);
			
			
		};
	}

}
