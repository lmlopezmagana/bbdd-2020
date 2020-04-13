package com.salesianostriana.dam.segundoejemplospringcore;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.salesianostriana.dam.segundoejemplospringcore.saludadores.ISaludador;
import com.salesianostriana.dam.segundoejemplospringcore.traductores.ITranslator;

/**
 * Segundo ejemplo de Spring Core, en el cual se realiza la configuración
 * a través de anotaciones
 * @author lmlopez
 *
 */
public class App {
	public static void main(String[] args) {

		// Levantamos el contenedor de inversión de control
		ApplicationContext appContext = new AnnotationConfigApplicationContext(
				"com.salesianostriana.dam.segundoejemplospringcore");

		// Creamos un bean en función del tipo.
		// Se ha definido como un @Component
		ISaludador comoMeDeLaGana = appContext.getBean(ISaludador.class);

		// Trabajamos con él.
		comoMeDeLaGana.setSaludo("Hola a todos los bicharracos!");
		comoMeDeLaGana.printSaludo();
		comoMeDeLaGana.printSaludoTraducido();

		// Creamos otro bean, de otro tipo diferente.
		// Este se ha definido como un @Service
		ITranslator traductor = appContext.getBean(ITranslator.class);

		// Trabajamos con ambos beans.
		comoMeDeLaGana.setSaludo(traductor.translate("Hola a todos los bicharracos!"));
		comoMeDeLaGana.printSaludo();
		
		// Comprobamos los beans que existen ahora mismo en el contexto.
		System.out.println("\n\nListado con todos los beans definidos en el contenedor\n");
		for(String s: appContext.getBeanDefinitionNames())
				System.out.println(s);
		

		// Cerramos el contenedor.
		((AnnotationConfigApplicationContext) appContext).close();
	}
}
