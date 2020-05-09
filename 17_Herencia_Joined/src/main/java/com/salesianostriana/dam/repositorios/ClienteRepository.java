/**
 * 
 */
package com.salesianostriana.dam.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesianostriana.dam.modelo.Cliente;

/**
 * @author luismi
 *
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	@Query("select c from Cliente c where TYPE(c) = Cliente")
	public List<Cliente> clientesNoVip();

}
