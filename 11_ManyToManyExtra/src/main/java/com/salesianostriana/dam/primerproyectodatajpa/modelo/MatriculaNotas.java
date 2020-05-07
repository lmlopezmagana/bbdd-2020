package com.salesianostriana.dam.primerproyectodatajpa.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Esta clase sirve para indicar que un alumno está matriculado en una asignatura
 * y además para guardar las notas trimestrales.
 * @author luismi
 *
 */
@Data @NoArgsConstructor
@Entity
public class MatriculaNotas {
	
	@EmbeddedId
	private NotasPK id = new NotasPK();
	
	@ManyToOne
	@MapsId("alumno_id")
	@JoinColumn(name="alumno_id")
	private Alumno alumno;	
	
	
	@ManyToOne
	@MapsId("asignatura_id")
	@JoinColumn(name="asignatura_id")
	private Asignatura asignatura;
	
	private int primeraEv;
	private int segundaEv;
	private int terceraEv;
	private int notaFinal;
	
	
	public MatriculaNotas(Alumno alumno, Asignatura asignatura, int primeraEv, int segundaEv, int terceraEv, int notaFinal) {
		this.alumno = alumno;
		this.asignatura = asignatura;
		this.primeraEv = primeraEv;
		this.segundaEv = segundaEv;
		this.terceraEv = terceraEv;
		this.notaFinal = notaFinal;
	}
	
	public MatriculaNotas(Alumno alumno, Asignatura asignatura) {
		this.alumno = alumno;
		this.asignatura = asignatura;
	}
	
	
	

}