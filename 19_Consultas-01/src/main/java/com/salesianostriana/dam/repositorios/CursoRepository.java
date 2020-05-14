/**
 * 
 */
package com.salesianostriana.dam.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesianostriana.dam.modelo.Curso;

/**
 * Repositorio que gestiona todos los cursos de nuestro sistema
 * @author luismi
 *
 */
public interface CursoRepository extends JpaRepository<Curso, Long> {

	
	/**
	 * Buscar todos los cursos cuyo campo nombre contiene el valor proporcionado
	 * @param nombre
	 * @return Cursos que contienen dicha cadena de caracteres o una lista vacía
	 */
	List<Curso> findByNombreContains(String nombre);
	
	
	
}
