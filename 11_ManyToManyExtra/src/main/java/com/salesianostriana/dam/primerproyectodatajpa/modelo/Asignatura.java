package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Asignatura {
	
	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private String profesor;
	
	@ManyToOne
	private Curso curso;
	
	// Mantenemos esta lista, pero no añadimos helpers
	// Si queremos rellenar la lista, realizamos un JOIN FETCH
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="asignatura", fetch = FetchType.EAGER)
	private List<MatriculaNotas> matriculaNotas = new ArrayList<>();
	
	public Asignatura(String nombre, String profesor) {
		this.nombre = nombre;
		this.profesor = profesor;
	}

	public Asignatura(String nombre, String profesor, Curso curso) {
		this.nombre = nombre;
		this.profesor = profesor;
		this.curso = curso;
	}
	
	

}
