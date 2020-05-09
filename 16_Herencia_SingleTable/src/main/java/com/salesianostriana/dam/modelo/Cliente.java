/**
 * 
 */
package com.salesianostriana.dam.modelo;

import javax.persistence.DiscriminatorValue;
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
@DiscriminatorValue("C")
public class Cliente extends Usuario{

	public Cliente(String fullName, String username, String password) {
		super(fullName, username, password);
	}
	
}
