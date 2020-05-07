package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public class Alumno {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String nombre, apellidos, email;
	
	@ManyToOne
	private Curso curso;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		joinColumns = @JoinColumn(name="alumno_id"),
		inverseJoinColumns = @JoinColumn(name="asignatura_id")
	)
	private List<Asignatura> asignaturas = new ArrayList<>();
	
	
	public Alumno(String n, String a, String e) {
		this.nombre = n;
		this.apellidos = a;
		this.email = e;
	}

	/** MÉTODOS HELPERS **/
	
	public void addAsignatura(Asignatura a) {
		asignaturas.add(a);
		a.getAlumnos().add(this);
	}
	
	public void removeAsignatura(Asignatura a) {
		asignaturas.remove(a);
		a.getAlumnos().remove(this);
	}
	

}
