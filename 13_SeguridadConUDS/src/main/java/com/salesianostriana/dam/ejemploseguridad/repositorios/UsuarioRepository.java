package com.salesianostriana.dam.ejemploseguridad.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesianostriana.dam.ejemploseguridad.seguridad.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findFirstByEmail(String email);
	
	
}
