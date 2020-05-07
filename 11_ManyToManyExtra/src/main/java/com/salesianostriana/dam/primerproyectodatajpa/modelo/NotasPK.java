package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luismi
 *
 */
@Data @NoArgsConstructor
@Embeddable
public class NotasPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8682909319466153524L;
	
	long alumno_id;
	
	long asignatura_id;
	
	
	

}