/**
 * 
 */
package com.salesianostriana.dam.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.ClienteVip;
import com.salesianostriana.dam.repositorios.ClienteVipRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class ClienteVipServicio extends BaseService<ClienteVip, Long, ClienteVipRepository>{

	public ClienteVipServicio(ClienteVipRepository repo) {
		super(repo);
	}

	public List<ClienteVip> vipsPorFecha(LocalDate inicio, LocalDate fin) {
		return repositorio.findByFechaVipBetween(inicio, fin);
	}
	
}
