package com.salesianostriana.dam.sessionscope.model.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.sessionscope.model.Producto;
import com.salesianostriana.dam.sessionscope.model.servicios.base.BaseService;
import com.salesianostriana.dam.sessionscope.repos.ProductoRepository;

@Service
public class ProductoServicio extends BaseService<Producto, Long, ProductoRepository>{

	public ProductoServicio(ProductoRepository repo) {
		super(repo);
	}
	
	
}
