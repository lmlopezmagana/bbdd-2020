/**
 * 
 */
package com.salesianostriana.dam.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Cliente;
import com.salesianostriana.dam.repositorios.ClienteRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class ClienteServicio extends BaseService<Cliente, Long, ClienteRepository>{
	
	public ClienteServicio(ClienteRepository repo) {
		super(repo);
	}

	public Cliente findByUsername(String username) {
		return repositorio.findByUsername(username);
	}

}
