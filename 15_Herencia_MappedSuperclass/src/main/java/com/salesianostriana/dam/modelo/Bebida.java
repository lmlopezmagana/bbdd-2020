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
public class Bebida extends Producto{

	// Tamaño de la bebida expresado en centilitros
	private int tamanio;

	/**
	 * @param tamanio
	 */
	public Bebida(String nombre, float precio, int tamanio) {
		super(nombre, precio);
		this.tamanio = tamanio;
	}
	
	
	
}
