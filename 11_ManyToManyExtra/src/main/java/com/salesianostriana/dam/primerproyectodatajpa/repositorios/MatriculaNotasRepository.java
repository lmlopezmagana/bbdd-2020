package com.salesianostriana.dam.primerproyectodatajpa.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesianostriana.dam.primerproyectodatajpa.modelo.MatriculaNotas;
import com.salesianostriana.dam.primerproyectodatajpa.modelo.NotasPK;

public interface MatriculaNotasRepository extends JpaRepository<MatriculaNotas, NotasPK>{

}

