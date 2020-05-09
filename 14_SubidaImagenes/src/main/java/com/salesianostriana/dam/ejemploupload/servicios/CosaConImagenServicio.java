package com.salesianostriana.dam.ejemploupload.servicios;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.salesianostriana.dam.ejemploupload.modelo.CosaConImagen;
import com.salesianostriana.dam.ejemploupload.repositorios.CosaConImagenRepository;
import com.salesianostriana.dam.ejemploupload.servicios.base.BaseService;
import com.salesianostriana.dam.ejemploupload.upload.DBStorageService;

@Service
public class CosaConImagenServicio 
		extends BaseService<CosaConImagen, Long, CosaConImagenRepository>{
	
	
	private final DBStorageService dbStorageService;

	public CosaConImagenServicio(CosaConImagenRepository repo, 
			DBStorageService dbStoreService
			) {
		super(repo);
		this.dbStorageService = dbStoreService;
	}
	
	/**
	 * Método que permite guardar una entidad que tiene una imagen.
	 * 
	 * @param c
	 * @param imagen
	 * @return
	 */
	public CosaConImagen save(CosaConImagen c, MultipartFile imagen) {
		// Pasos a seguir
		
		// 1) Transformar la imagen en un String
		String pathImagen = dbStorageService.store(imagen);
		// 2) Asignar esta cadena de caracteres con nuestra entidad
		c.setImagen(pathImagen);
		// 3) Almacenarla
		return this.save(c);
	}

	/**
	 * Antes de eliminar nuestra entidad, tenemos que eliminar la imagen asociada.
	 */
	@Override
	public void delete(CosaConImagen t) {
		String idImagen = t.getImagen();
		dbStorageService.delete(Long.valueOf(idImagen));
		super.delete(t);
	}

	/**
	 * Antes de eliminar nuestra entidad, tenemos que eliminar la imagen asociada.
	 */
	@Override
	public void deleteById(Long id) {
		CosaConImagen cosa = findById(id);
		if (cosa != null)
			delete(cosa);
	}
	
	
	
	

}
