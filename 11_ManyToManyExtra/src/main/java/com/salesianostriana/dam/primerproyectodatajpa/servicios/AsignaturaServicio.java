package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Asignatura;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.AsignaturaRepository;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.base.BaseService;

@Service
public class AsignaturaServicio extends BaseService<Asignatura, Long, AsignaturaRepository> {

	public AsignaturaServicio(AsignaturaRepository repo) {
		super(repo);
	}

}
