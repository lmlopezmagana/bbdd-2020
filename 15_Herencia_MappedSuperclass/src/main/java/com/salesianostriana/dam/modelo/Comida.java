/**
 * 
 */
package com.salesianostriana.dam.modelo;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luismi
 *
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
public class Comida extends Producto {
	
	private boolean esFrio;
	
	private String tipo;
	
	public Comida(String nombre, float precio, boolean esFrio, String tipo) {
		super(nombre, precio);
		this.esFrio = esFrio;
		this.tipo = tipo;
	}

}
