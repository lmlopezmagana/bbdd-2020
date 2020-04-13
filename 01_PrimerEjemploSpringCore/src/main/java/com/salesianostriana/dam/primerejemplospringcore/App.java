package com.salesianostriana.dam.primerejemplospringcore;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * (1) Primer ejemplo usando Spring Core.
 * 
 * Podemos ver como se crea un bean a través de XML, y como 
 * levantamos dicho contexto, para poder interactuar con el objeto.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// Creamos el contexto a través del fichero beans.xml
        ApplicationContext appContext = new ClassPathXmlApplicationContext("beans.xml");
        
        
        // Se puede crear el bean bien por nombre, o bien por tipo.
        
        //MiPrimerBean comoMeDeLaGana = (MiPrimerBean) appContext.getBean("saludator");
        ISaludador comoMeDeLaGana = appContext.getBean(ISaludador.class); 
        
        // Un bean no deja de ser un objeto, con el que podemos interactuar.
        comoMeDeLaGana.setSaludo("Hola a todos los bicharracos!");
        
        comoMeDeLaGana.printSaludo();
        
        System.out.println(comoMeDeLaGana);
        
        /*
         * Normalmente, los beans son de tipo SINGLETON, es decir, solamente exite
         * un objeto en memoria, aunque tengamos más de una referencia al mismo.
         */
        MiPrimerBean otroBean = (MiPrimerBean) appContext.getBean("saludator");

        System.out.println(otroBean);
        
        otroBean.printSaludo();
        
        // Cerramos el contexto.
        ((ClassPathXmlApplicationContext) appContext).close();
    }
}
