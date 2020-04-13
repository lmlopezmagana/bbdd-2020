/**
 * 
 */
package com.salesianostriana.dam.segundoejemplospringcore.saludadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesianostriana.dam.segundoejemplospringcore.traductores.ITranslator;

/**
 * Mi primer bean de 2020 con 1º DAM. ¡Cuan feliz me siento!
 * @author lmlopez
 *
 */
@Component("saludator")
public class MiPrimerBean implements ISaludador {

	private String saludo;
	
	@Autowired
	private ITranslator traductor;
	
	@Override
	public void setSaludo(String s) {
		this.saludo = s;
	}
	
	@Override
	public void printSaludo() {
		System.out.println(saludo);
	}

	@Override
	public void printSaludoTraducido() {
		System.out.println(traductor.translate(saludo));		
	}
	
	
}
