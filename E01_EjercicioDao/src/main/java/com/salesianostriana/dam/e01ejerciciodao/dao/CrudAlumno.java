package com.salesianostriana.dam.e01ejerciciodao.dao;

import java.util.List;

import com.salesianostriana.dam.e01ejerciciodao.modelo.Alumno;

public interface CrudAlumno {
	
	
	List<Alumno> findAll();
	
	Alumno findById(long id);
	
	void insert(Alumno alumno);
	
	void edit(Alumno alumno);
	
	void delete(Alumno alumno);
	

}
