package com.salesianostriana.dam.ejemploupload.upload.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesianostriana.dam.ejemploupload.upload.DBStorageService;

import lombok.RequiredArgsConstructor;

/**
 * Este controlador especial nos permite obtener una imagen a partir
 * del ID de un ImagenEntity. De esta forma, si el ImagenEntity tiene
 * un ID 1234, la url que debemos de colocar en la etiqueta img sería:
 * 
 * <img th:src="@{/files/1234}" />
 * 
 * @author lmlopez
 *
 */
@Controller
@RequiredArgsConstructor
public class ImageController {

	
	private final DBStorageService dbStoreService;
	
	/**
	 * Este método nos permite rescatar una imagen en base a su ID
	 * @param id
	 * @return
	 */
	@GetMapping("/files/{id}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
		Resource file = dbStoreService.loadAsResource(id);
		return ResponseEntity.ok().body(file);
	}
	
}
