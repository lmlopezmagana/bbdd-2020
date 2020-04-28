package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
@Entity
public class Curso {

	@Id @GeneratedValue
	private long id;
	
	private String nombre;
	private String tutor;
	
	
	public Curso(String nombre, String tutor) {
		this.nombre = nombre;
		this.tutor = tutor;
	}
	
	
	
	
}
