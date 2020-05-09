package com.salesianostriana.dam.ejemploupload.upload.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public class ImagenEntity {
	
	@Id @GeneratedValue
	private long id;
	
	@Lob
	private String contenido;
	
	public ImagenEntity(String img) {
		this.contenido = img;
	}

}
