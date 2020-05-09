/**
 * 
 */
package com.salesianostriana.dam.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Usuario {

	@Id
	@GeneratedValue
	private long id;
	
	private String fullName;
	
	private String username;
	private String password;
	
	public Usuario(String fullName, String username, String password) {
		this.fullName = fullName;
		this.username = username;
		this.password = password;
	}
	
}
