/**
 * 
 */
package com.salesianostriana.dam.servicios;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Alumno;
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
	
}
