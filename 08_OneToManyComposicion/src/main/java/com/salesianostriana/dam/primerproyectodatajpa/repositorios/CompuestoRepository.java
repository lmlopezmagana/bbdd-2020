package com.salesianostriana.dam.primerproyectodatajpa.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.Compuesto;

public interface CompuestoRepository 
			extends JpaRepository<Compuesto, Long> {
	
	@Query("select distinct c from Compuesto c join fetch c.componentes")
	List<Compuesto> findAllJoin();

}
