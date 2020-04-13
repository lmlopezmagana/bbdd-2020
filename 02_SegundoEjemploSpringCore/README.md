# (02) Segundo ejemplo con Spring Core

En este segundo ejemplo vamos a aprender a utilizar el segundo enfoque para configurar los beans: el uso de anotaciones.

## Estereotipos

Existen 4 posibles estereotipos para anotar un bean:

- `@Component`: es el tipo más básico. Denota que una clase es un bean. Idéntico a añadir un elemento `<bean>` en el fichero XML.
- `@Service`: anotación derivada de la anterior. Hace referencia a que este bean contiene métodos para realizar una determinada tarea en la capa de servicios.
- `@Repository`: anotación derivada de `@Component`. Indica que este bean está orientado al acceso a datos.
- `@Controller`: anotación derivada de `@Component`. Hace referencia a que este bean recibirá, de alguna manera, la interacción con el usuario.

En este ejemplo vamos a comprobar el uso de `@Component` y `@Service`. A lo largo de posteriores ejemplos, usaremos el resto de estereotipos.

## 1) Activar la configuración mediante anotaciones y el escaneo de componentes 

Para indicarle a Spring que queremos usar la configuración mediante anotaciones y que escanee automáticamente nuestros paquetes para encontrar las mismas, tenemos que definir en nuestro XML el siguiente código:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
					    http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/context 
						http://www.springframework.org/schema/context/spring-context-4.3.xsd">

	<context:component-scan base-package="com.salesianostriana.dam" />
		
</beans>
```

De esta forma:

- Activamos el uso de anotaciones
- Definimos un paquete "base", a partir del cual habilitamos a Spring a que busque _beans_ definidos a través de anotaciones.

## 2) Definición de los beans

Al igual que en el ejemplo anterior, los beans se definen a través de clases Java. En este caso, anotamos la clase a través del estereotipo correspondiente.

### Bean esterotipado como componente

Se trata del mismo bean del primer ejemplo, en este caso, definido como componente a través de la anotación `@Component`.

> Por defecto, cuando anotamos un bean con un estereotipo, el nombre del bean es el nombre de la clase en notación _camel case_. Si queremos modificar el nombre del bean, lo podemos añadir a través de la anotación. En este ejemplo, el nombre del bean sería `miPrimerBean`, pero se sustituye por `saludator`


```java
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
```

### Bean estereotipado como servicio

A lo largo de los próximos ejemplos aprenderemos mejor cuándo utilizar los estereotipos derivados, como `@Service`.

```java
@Service
public class SpanishEnglishTranslator implements ITranslator {

	@Override
	public String translate(String message) {

		String result = null;

		if (!message.isEmpty()) {
			switch (message.toLowerCase()) {
			case "hola a todos los bicharracos!":
				result = "Hello to all the bicharracos!!!";
				break;
			default:
				result = "I don't understand you";
			}
		}

		return result;
	}

}
```

Este servicio tiene un método que se encarga de _traducir_ un mensaje del español al inglés.

> Nótese que se trata de un traductor algo _dummy_.

## 3) Uso de los beans

Los beans definidos a través de anotaciones se puede utilizar de la misma forma que los que se definen a través de XML.

```java
// Levantamos el contenedor de inversión de control
ApplicationContext appContext = new AnnotationConfigApplicationContext("com.salesianostriana.dam.segundoejemplospringcore");

// Creamos un bean en función del tipo.
// Se ha definido como un @Component
ISaludador comoMeDeLaGana = appContext.getBean(ISaludador.class);

// Resto del código ...

// Creamos otro bean, de otro tipo diferente.
// Este se ha definido como un @Service
ITranslator traductor = appContext.getBean(ITranslator.class);

// Resto del código ...		

// Cerramos el contenedor.
((AnnotationConfigApplicationContext) appContext).close();

```

