package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Compuesto;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.CompuestoRepository;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.base.BaseService;

@Service
public class CompuestoServicio extends BaseService<Compuesto, Long, CompuestoRepository> {

	public CompuestoServicio(CompuestoRepository repo) {
		super(repo);
	}

	@Override
	public List<Compuesto> findAll() {
		return this.repositorio.findAllJoin();
	}
	
	

}
