package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Asignatura;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.MatriculaNotas;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.NotasPK;
import com.salesianostriana.dam.primerproyectodatajpa.repositorios.MatriculaNotasRepository;
import com.salesianostriana.dam.primerproyectodatajpa.servicios.base.BaseService;

@Service
public class MatriculaNotasServicio extends BaseService<MatriculaNotas, NotasPK, MatriculaNotasRepository> {

	private final AlumnoServicio alumnoServicio;

	public MatriculaNotasServicio(MatriculaNotasRepository repo, AlumnoServicio alumnoServicio) {
		super(repo);
		this.alumnoServicio = alumnoServicio;
	}

	// Método que matricula a un alumno en todas las asignaturas de un curso
	public Alumno matricularAlumno(Alumno alumno, Curso curso) {
		curso.addAlumno(alumno);
		alumnoServicio.save(alumno);
		for (Asignatura asignatura : curso.getAsignaturas()) {
			MatriculaNotas m = new MatriculaNotas(alumno, asignatura);
			this.save(m);
		}

		// Ejecutamos la consulta para "refrescar" el alumno
		// con sus asociaciones, de manera que las colecciones
		// de tipo EAGER vendrán rellenas de datos sin
		// necesidad de métodos helper.
		return alumnoServicio.findById(alumno.getId());

	}

	// Método para poner la nota de un trimestre
	// En caso de no esté matriculado en la asignatura o no exista el trimestre
	// devolvemos null
	public Alumno notaTrimestral(Alumno alumno, Asignatura asignatura, int trimestre, int nota) {

		NotasPK pk = new NotasPK();
		pk.setAlumno_id(alumno.getId());
		pk.setAsignatura_id(asignatura.getId());
		MatriculaNotas m = this.findById(pk);
		if (m != null) {
			boolean modificado = false;
			switch (trimestre) {
			case 1:
				m.setPrimeraEv(nota);
				modificado = true;
				break;
			case 2:
				m.setSegundaEv(nota);
				modificado = true;
				break;
			case 3:
				m.setTerceraEv(nota);
				modificado = true;
				break;
			case 4:
				m.setNotaFinal(nota);
				modificado = true;
				break;
			default:
				return null;
			}
			if (modificado) {
				this.edit(m);
				return alumnoServicio.findById(alumno.getId());
			}
		}

		return null;

	}

}
