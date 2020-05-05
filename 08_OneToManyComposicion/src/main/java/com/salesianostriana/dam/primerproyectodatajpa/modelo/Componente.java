package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Componente {
	
	@Id @GeneratedValue
	private Long id;
	
	private String atr1;
	private int atr2;
	private double atr3;
	
	@ManyToOne
	private Compuesto compuesto;
	
	
	
	public Componente(String atr1, int atr2, double atr3) {
		this.atr1 = atr1;
		this.atr2 = atr2;
		this.atr3 = atr3;
	}
	
	

}
