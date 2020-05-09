package com.salesianostriana.dam.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Cliente;
import com.salesianostriana.dam.repositorios.ClienteRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * 
 * @author luismi
 *
 */
@Service
public class ClienteServicio extends BaseService<Cliente, Long, ClienteRepository>{

	public ClienteServicio(ClienteRepository repo) {
		super(repo);
	}

	public List<Cliente> noVips() {
		return repositorio.clientesNoVip();
	}
	
}
