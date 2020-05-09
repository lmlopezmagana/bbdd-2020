/**
 * 
 */
package com.salesianostriana.dam.modelo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luismi
 *
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String nombre;
	
	private float precio;
	
	public Producto(String nombre, float precio) {
		this.nombre = nombre;
		this.precio = precio;
	}
	

}
