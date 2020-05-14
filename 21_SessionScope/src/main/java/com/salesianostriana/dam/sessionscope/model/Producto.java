package com.salesianostriana.dam.sessionscope.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public class Producto {
	
	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private float precio;
	
	public Producto(String n, float p) {
		this.nombre = n;
		this.precio = p;
	}
	
}
