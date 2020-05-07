package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Alumno;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Asignatura;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;

import lombok.RequiredArgsConstructor;

/**
 * Ejemplo de servicio que implementa algo de lógica de negocio
 * Le pasamos un alumno y un curso, y va a "matricular" al alumno
 * en el curso, y en todas sus asignaturas.
 * @author lmlopez
 *
 */
@Service
@RequiredArgsConstructor
public class MatriculaServicio {
	
	private final AlumnoServicio alumnoServicio;
	private final MailService mailService;
	
	public Alumno matriculaAlumnoEnTodasAsignaturasDeUnCurso(Alumno a, Curso c) {
		
		if (a != null && c != null) {
			// Está el alumno almacenado? Y el curso? Si no, lo guardamos
			if (a.getId() < 1) {
				alumnoServicio.save(a);
			}
						
			// Añadimos el alumno al curso.
			c.addAlumno(a);
			
			// Almacenamos en la base de datos
			alumnoServicio.edit(a);
			
			// Obtenemos todas las asignaturas del curso y matriculamos al alumno
			for (Asignatura asignatura : c.getAsignaturas()) {
				a.addAsignatura(asignatura);
			}
			// "Matriculamos al alumno en las asignaturas guardándolo en la base de datos."
			alumnoServicio.edit(a);
			
			/*
			 * Las líneas anteriores se podrían ajustar y resumir un poco
			 *
			 */
//			c = cursoServicio.findByIdEager(c.getId());
//			c.addAlumno(a);
//			for (Asignatura asignatura : c.getAsignaturas()) {
//				a.addAsignatura(asignatura);
//			}
//			alumnoServicio.edit(a);
			
			// Enviamos el email para confirmar la matrícula
			StringBuilder cuerpo = new StringBuilder();
			cuerpo.append("El alumno " + a.getNombre() + " " + a.getApellidos());
			cuerpo.append(" se ha matriculado del curso " + c.getNombre());
			cuerpo.append(" y de las siguientes asignaturas: ");
			cuerpo.append(
					a.getAsignaturas().stream()
						.map(Asignatura::getNombre)
						.collect(Collectors.joining(", "))	
					);
			cuerpo.append(" Un saludo");
			mailService.send(a.getEmail(), "Confirmación de matrícula", cuerpo.toString());
				
			return a;
			
		} else
			return null;
		
		
	}
	

}
