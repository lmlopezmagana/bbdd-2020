package com.salesianostriana.dam.e01ejerciciodao.servicios;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.e01ejerciciodao.dao.CrudAlumno;
import com.salesianostriana.dam.e01ejerciciodao.modelo.Alumno;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlumnoServicio {
	
	private final CrudAlumno c;
	
	
	/**
	 * Busca a todos los alumnos
	 * @return lista de alumnos
	 */
	public List<Alumno> findAll(){
		return c.findAll();
	}
	/**
	 * Busca un alumno por su id
	 * @param id
	 * @return un solo alumno.
	 */
	public Alumno findById(long id) {
		return c.findById(id);
	}
	/**
	 * A�ade un alumno a la lista
	 * @param alumno
	 */
	public void insert(Alumno alumno) {
		c.insert(alumno);
	}
	/**
	 * Modifica un alumno
	 * @param alumno
	 */
	public void edit(Alumno alumno) {
		c.edit(alumno);
	}
	/**
	 * Elimina un alumno
	 * @param alumno
	 */
	public void delete (Alumno alumno) {
		c.delete(alumno);
	}
	/**
	 * Ordena la lista de alumnos por orden alfab�tico
	 * @return la lista ordenada
	 */
	public List<Alumno> getByOrdenAlfabetico(){
		// @formatter:off
		return c.findAll()
					.stream()
					.sorted(Comparator.comparing(Alumno::getApellidos))
					.collect(Collectors.toList());
 
		// @formatter:on
		
		// Versión sin streams y lambdas
		/*
		List<Alumno> alumnos = new ArrayList<>(c.findAll());
		Collections.sort(alumnos, new Comparator<Alumno>() {

			@Override
			public int compare(Alumno o1, Alumno o2) {
				return o1.getApellidos().compareTo(o2.getApellidos());
			}
			
		});
		
		
		return alumnos;
		*/
	}
	/**
	 * Ordena los alumnos por edad
	 * @return la lista ordenada.
	 */
	public List<Alumno> getByEdad(){
		// @formatter:off
		return c.findAll()
				.stream()
				.sorted(Comparator.comparing(Alumno::getEdad))
				.collect(Collectors.toList());
 
		// @formatter:on

	}
	
	/**
	 * Crea un ID empezando por 100 si la lista está vacía. Si no, busca el ID más alto 
	 * @return el siguiente ID disponble
	 */
	public long makeID() {
		if(c.findAll().isEmpty()) {
			return 100;
		}else {
			return c.findAll().stream().max(Comparator.comparing(Alumno::getId)).get().getId()+1;
		}
	}
	
	

}
