/**
 * 
 */
package com.salesianostriana.dam.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luismi
 *
 */
@Data @NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String email;
	
	private String nombre;
	private String apellidos;
	
	
	/**
	 * @param email
	 * @param nombre
	 * @param apellidos
	 */
	public Cliente(String email, String nombre, String apellidos) {
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	
	
	
}
