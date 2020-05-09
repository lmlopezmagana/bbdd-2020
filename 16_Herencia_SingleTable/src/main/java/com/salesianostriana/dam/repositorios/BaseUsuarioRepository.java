package com.salesianostriana.dam.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.salesianostriana.dam.modelo.Usuario;

/**
 * 
 * @author luismi
 *
 */
@NoRepositoryBean
public interface BaseUsuarioRepository<T extends Usuario> extends JpaRepository<T, Long> {
	
	public T findByUsername(String Username);

}
