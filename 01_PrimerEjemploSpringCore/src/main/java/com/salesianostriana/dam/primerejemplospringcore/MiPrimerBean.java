/**
 * 
 */
package com.salesianostriana.dam.primerejemplospringcore;

/**
 * Mi primer bean de 2020 con 1º DAM. ¡Cuan feliz me siento!
 * @author lmlopez
 *
 */
public class MiPrimerBean implements ISaludador {

	private String saludo;
	
	public void setSaludo(String s) {
		this.saludo = s;
	}
	
	public void printSaludo() {
		System.out.println(saludo);
	}
	
	
}
