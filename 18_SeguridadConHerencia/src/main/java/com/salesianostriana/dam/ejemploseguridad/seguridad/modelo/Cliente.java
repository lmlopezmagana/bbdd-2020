package com.salesianostriana.dam.ejemploseguridad.seguridad.modelo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class Cliente extends Usuario{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDate fechaNacimiento;

	public Cliente(String nombre, String apellidos, String email, String password, 
			LocalDate fechaNacimiento) {
		super(nombre, apellidos, email, password);
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	
	

}
