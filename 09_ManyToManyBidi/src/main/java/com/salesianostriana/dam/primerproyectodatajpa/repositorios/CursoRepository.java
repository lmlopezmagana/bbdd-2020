package com.salesianostriana.dam.primerproyectodatajpa.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{
	
	@Query("select distinct c from Curso c left join fetch c.asignaturas")
	List<Curso> findAllWithAsig();
	
	

}
