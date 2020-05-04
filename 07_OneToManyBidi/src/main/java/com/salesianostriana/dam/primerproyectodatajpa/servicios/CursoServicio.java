package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.CursoRepository;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.base.BaseService;

@Service
public class CursoServicio extends BaseService<Curso, Long, CursoRepository> {

	public CursoServicio(CursoRepository repo) {
		super(repo);
	}

}
