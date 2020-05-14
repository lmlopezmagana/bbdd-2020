/**
 * 
 */
package com.salesianostriana.dam.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Alumno;
import com.salesianostriana.dam.modelo.Curso;
import com.salesianostriana.dam.repositorios.AlumnoRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class AlumnoServicio extends BaseService<Alumno, Long, AlumnoRepository>{
	
	public AlumnoServicio(AlumnoRepository repo) {
		super(repo);
	}

	@Autowired
	private CursoServicio cursoServicio;

	/**
	 * Devuelve 5 alumnos, ordenados por fecha de nacimiento descendente
	 * cuyo apellido sea igual al proporcionado
	 */
	public List<Alumno> cincoPorApellido(String apellido) {
		return repositorio.findTop5ByApellido1OrderByFechaNacimientoDesc(apellido);
	}
	
	/**
	 * Obtiene desde el repositorio como mucho 3 alumnos cuyo primer o segundo 
	 * apellido sea el proporcionado.
	 * @return Una lista con el nombre completo de los alumnos como String.
	 */
	@Transactional
	public List<String> nombreAlumnosContieneApellido(String apellido) {
		return repositorio
				.findTop3ByApellido1ContainsOrApellido2Contains(apellido, apellido)
				.map(a -> a.getNombre() + " " + a.getApellido1() + " " + a.getApellido2())
				.collect(Collectors.toList());
	}
	
	/**
	 * Envoltorio para el método correspondiente del repositorio
	 */
	public List<Alumno> alumnosSinCurso() {
		return repositorio.encuentraAlumnoSinCurso();
	}
	
	/**
	 * Este es un ejemplo de un método de servicio que no utiliza la consulta del repositorio tal cual. 
	 * El método recibe la fecha como un LocalDate, y el nombre del curso como una cadena de caracteres.
	 * Sin embargo, el método del repositorio necesita una instancia de Curso. Para obtenerla, utilizamos
	 * otro método de consulta, buscarPorNombreExacto, y si la encontramos, la utilizamos para ejecutar la
	 * búsqueda de alumnos con fecha de nacimiento posterior a una dada par un curso dado.
	 */
	public List<Alumno> alumnosFechaNacimientoDespuesDe(LocalDate fecha, String nombreCurso) {
		List<Alumno> result = new ArrayList<Alumno>();
		
		Curso curso = cursoServicio.buscarPorNombreExacto(nombreCurso);
		
		if (curso != null) {
			result = repositorio.alumnosNacidosDespuesDe(fecha, curso);
		}
		
		return result;
	}
	
}
