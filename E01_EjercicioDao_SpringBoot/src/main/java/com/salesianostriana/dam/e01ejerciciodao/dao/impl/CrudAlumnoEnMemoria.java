package com.salesianostriana.dam.e01ejerciciodao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.e01ejerciciodao.dao.CrudAlumno;
import com.salesianostriana.dam.e01ejerciciodao.modelo.Alumno;

@Repository
public class CrudAlumnoEnMemoria implements CrudAlumno {
	
	private List<Alumno> listaAlumnos;
	
	
	public CrudAlumnoEnMemoria() {
		listaAlumnos = new ArrayList<>();
	}

	@Override
	public List<Alumno> findAll() {
		return listaAlumnos;
	}

	@Override
	public Alumno findById(long id) {
		// Versión con streams + lambda
		return listaAlumnos.stream().filter(x -> x.getId() == id).findFirst().get();
		
		// Versión sin lambdas
		/* bool encontrado = false;
		 * Alumno resultado = null;
		for(int i = 0; i < listaAlumnos.size() && !encontrado; i++) {
			if (listaAlumnos.get(i).getId() == id) {
				encontrado = true;
				resultado = listaAlumnos.get(i);
			}
		}
		return resultado;
		*/
	}

	@Override
	public void insert(Alumno alumno) {
		listaAlumnos.add(alumno);
	}

	@Override
	public void edit(Alumno alumno) {
		
		// @formatter:off
		
		listaAlumnos
		.stream()
		.filter(x -> x.getId() == alumno.getId())
		.map((Alumno x) -> {
			x.setApellidos(alumno.getApellidos());
			x.setNombre(alumno.getNombre());
			x.setEmail(alumno.getEmail());
			x.setFechaNacimiento(alumno.getFechaNacimiento());
			
			return x;
		});
	
		// @formatter:on
		
	}

	@Override
	public void delete(Alumno alumno) {
		listaAlumnos.remove(alumno);		
	}

}
