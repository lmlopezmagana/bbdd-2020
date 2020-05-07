package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String nombre, apellidos, email;
	
	@ManyToOne
	private Curso curso;
	
	// Mantenemos esta lista, pero no añadimos helpers
	// Si queremos rellenar la lista, realizamos un JOIN FETCH
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="alumno", fetch = FetchType.EAGER)
	private List<MatriculaNotas> matriculaNotas = new ArrayList<>();
	
	
	public Alumno(String n, String a, String e) {
		this.nombre = n;
		this.apellidos = a;
		this.email = e;
	}

	
	

}
