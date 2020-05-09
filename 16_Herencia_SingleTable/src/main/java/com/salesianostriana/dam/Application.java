package com.salesianostriana.dam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.modelo.Admin;
import com.salesianostriana.dam.modelo.Cliente;
import com.salesianostriana.dam.servicios.AdminServicio;
import com.salesianostriana.dam.servicios.ClienteServicio;
import com.salesianostriana.dam.servicios.UsuarioServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner init(AdminServicio adminServicio, ClienteServicio clienteServicio, UsuarioServicio usuarioServicio) {
		return args -> {
			
			Admin admin = new Admin("Luis Miguel López", "lmlopez", "123456");
			adminServicio.save(admin);
			
			
			Cliente cliente1 = new Cliente("Ángel Naranjo", "anaranjo", "123456");
			Cliente cliente2 = new Cliente("Miguel Campos", "mcampos", "123456");
			
			clienteServicio.save(cliente1);
			clienteServicio.save(cliente2);
			
			System.out.println("ADMINISTRADORES");
			adminServicio.findAll().forEach(System.out::println);
			
			System.out.println("\nCLIENTES");
			clienteServicio.findAll().forEach(System.out::println);
			
			System.out.println("\nTODOS");
			usuarioServicio.findThemAll().forEach(System.out::println);
			
			System.out.println("\nBÚSQUEDA POR NOMBRE DE USUARIO");
			System.out.println(clienteServicio.findByUsername("mcampos"));
			
		};
	}
	
	

}
