/**
 * 
 */
package com.salesianostriana.dam.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesianostriana.dam.modelo.ClienteVip;

/**
 * @author luismi
 *
 */
public interface ClienteVipRepository extends JpaRepository<ClienteVip, Long>{
	
	List<ClienteVip> findByFechaVipBetween(LocalDate fechaVipStart, LocalDate fechaVipEnd);


}
