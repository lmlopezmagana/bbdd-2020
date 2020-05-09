package com.salesianostriana.dam.ejemploupload.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public class CosaConImagen {
	
	@Id @GeneratedValue
	private long id;
	
	private String nombre;
	
	private String imagen;
	
	
	public CosaConImagen(String nombre) {
		this.nombre = nombre;
	}
	

}
