package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.AlumnoRepository;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.base.BaseService;

@Service
public class AlumnoServicio extends BaseService<Alumno, Long, AlumnoRepository>{

	public AlumnoServicio(AlumnoRepository repo) {
		super(repo);
	}

	
}
