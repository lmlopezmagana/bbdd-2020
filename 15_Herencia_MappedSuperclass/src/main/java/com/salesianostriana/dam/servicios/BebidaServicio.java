/**
 * 
 */
package com.salesianostriana.dam.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Bebida;
import com.salesianostriana.dam.repositorios.BebidaRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class BebidaServicio extends BaseService<Bebida, Long, BebidaRepository> {

	public BebidaServicio(BebidaRepository repo) {
		super(repo);
	}

}
