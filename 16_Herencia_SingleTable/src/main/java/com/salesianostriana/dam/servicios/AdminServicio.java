/**
 * 
 */
package com.salesianostriana.dam.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.modelo.Admin;
import com.salesianostriana.dam.repositorios.AdminRepository;
import com.salesianostriana.dam.servicios.base.BaseService;

/**
 * @author luismi
 *
 */
@Service
public class AdminServicio extends BaseService<Admin, Long, AdminRepository> {

	public AdminServicio(AdminRepository repo) {
		super(repo);
	}

}
