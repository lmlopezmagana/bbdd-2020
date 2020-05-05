package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Compuesto {
	
	@Id @GeneratedValue
	private Long id;
	
	private String prop1;
	private String prop2;
	private LocalDate fecha;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="compuesto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Componente> componentes = new ArrayList<>();

	public Compuesto(String prop1, String prop2, LocalDate fecha) {
		this.prop1 = prop1;
		this.prop2 = prop2;
		this.fecha = fecha;
	}
	
	
	/**
	 * MÉTODOS AUXILIARES Ó HELPERS
	 * 
	 */
	
	public void addComponente(Componente c) {
		c.setCompuesto(this);
		this.componentes.add(c);
	}
	
	
	public void removeComponente(Componente c) {
		this.componentes.remove(c);
		c.setCompuesto(null);
	}
	

}
