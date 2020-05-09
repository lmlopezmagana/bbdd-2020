/**
 * 
 */
package com.salesianostriana.dam.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Comida;
import com.salesianostriana.dam.repositorios.ComidaRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class ComidaServicio extends BaseService<Comida, Long, ComidaRepository>{

	public ComidaServicio(ComidaRepository repo) {
		super(repo);
	}

}
