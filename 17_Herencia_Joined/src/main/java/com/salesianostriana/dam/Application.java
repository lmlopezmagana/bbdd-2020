package com.salesianostriana.dam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.salesianostriana.dam.modelo.Cliente;
import com.salesianostriana.dam.modelo.ClienteVip;
import com.salesianostriana.dam.servicios.ClienteServicio;
import com.salesianostriana.dam.servicios.ClienteVipServicio;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner init(ClienteServicio clienteServicio, ClienteVipServicio clienteVipServicio) {
		return args -> {

			List<Cliente> clientes = Arrays.asList(new Cliente("email@email.com", "Cliente 1", "12345"),
					new Cliente("email2@email.com", "Cliente 2", "12345"));
			
			ClienteVip clienteVip = new ClienteVip("emailvip@email.com", "Cliente Vip", "12345", LocalDate.now());
			
			clientes.forEach(clienteServicio::save);
			
			clienteServicio.save(clienteVip);
			
			System.out.println("\nTODOS LOS CLIENTES");
			clienteServicio.findAll().forEach(System.out::println);
			
			System.out.println("\nCLIENTES NO VIPS");
			clienteServicio.noVips().forEach(System.out::println);
			
			System.out.println("\nCLIENTES VIPS POR FECHA");
			clienteVipServicio.vipsPorFecha(LocalDate.now().minusDays(1), LocalDate.now()).forEach(System.out::println);

			
			
			
		};
	}

}
