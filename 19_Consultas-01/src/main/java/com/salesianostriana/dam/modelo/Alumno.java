/**
 * 
 */
package com.salesianostriana.dam.modelo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que modela los datos más básicos de un alumno
 * 
 * @author lmlopez
 *
 */
@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private LocalDate fechaNacimiento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Curso curso;

	
	
	/**
	 * @param nombre
	 * @param apellido1
	 * @param apellido2
	 * @param email
	 * @param fechaNacimiento
	 * @param curso
	 */
	public Alumno(String nombre, String apellido1, String apellido2, String email, LocalDate fechaNacimiento,
			Curso curso) {
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.curso = curso;
	}
	

}
