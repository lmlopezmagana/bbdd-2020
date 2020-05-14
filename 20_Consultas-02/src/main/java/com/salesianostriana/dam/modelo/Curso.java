/**
 * 
 */
package com.salesianostriana.dam.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase que modela un Curso que tiene muchos alumnos
 * @author luismi
 *
 */
@Data @NoArgsConstructor
@Entity
public class Curso {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String nombre;
	private String tutor;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy="curso")
	private List<Alumno> alumnos = new ArrayList<>();
	
	/**
	 * @param nombre
	 * @param tutor
	 */
	public Curso(String nombre, String tutor) {
		this.nombre = nombre;
		this.tutor = tutor;
	}
	
	
	/********************************************/
	/* MÉTODOS AUXILIARES					    */
	/********************************************/
	
	/**
	 * Método auxiliar para el tratamiento bidireccional de la asociación. Añade a un alumno
	 * a la colección de alumnos de un curso, y asigna a dicho alumno este curso como el suyo.
	 * @param a
	 */
	public void addAlumno(Alumno a) {
		this.alumnos.add(a);
		a.setCurso(this);
	}
	
	/**
	 * Método auxiliar para el tratamiento bidireccional de la asociación. Elimina un alumno
	 * de la colección de alumnos de un curdso, y desasigna a dicho alumno el curso, dejándolo como nulo.
	 * @param a
	 */
	public void removeAlumno(Alumno a) {
		this.alumnos.remove(a);
		a.setCurso(null);
	}
}
