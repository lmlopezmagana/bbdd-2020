/**
 * 
 */
package com.salesianostriana.dam.modelo;

import java.time.LocalDate;

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
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class ClienteVip extends Cliente {

	private LocalDate fechaVip;
	
	public ClienteVip(String email, String nombre, String apellidos, LocalDate fechaVip) {
		super(email, nombre, apellidos);
		this.fechaVip = fechaVip;
	}
	
}
