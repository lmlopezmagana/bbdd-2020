package com.salesianostriana.dam.ejemploseguridad.servicios;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.ejemploseguridad.repositorios.UsuarioRepository;
import com.salesianostriana.dam.ejemploseguridad.seguridad.modelo.Usuario;
import com.salesianostriana.dam.ejemploseguridad.servicios.base.BaseService;

@Service
public class UsuarioServicio extends BaseService<Usuario, Long, UsuarioRepository>{

	public UsuarioServicio(UsuarioRepository repo) {
		super(repo);
	}
	
	public Optional<Usuario> buscarPorEmail(String email) {
		return repositorio.findFirstByEmail(email);
	}

}
