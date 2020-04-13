# (01) Primer ejemplo con Spring Core

Como hemos podido aprender a lo largo de las diferentes explicaciones, Spring es, entre otras cosas, un framework que nos permite realizar la inversión de control a través de un mecanismo llamado inyección de dependencias. Una de las formas de realizar este inyección es a través de la descripción de los beans (clases que participan de este mecanismo de inyección) en un fichero XML.

## 1) Integración

Para poder integrar Spring en un nuevo proyecto, lo podemos hacer añadiendo la siguiente dependencia a un proyecto Maven:

```xml
<dependency>
    <groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>5.2.5.RELEASE</version>
</dependency>
```

## 2) Cómo definir y crear los beans

El mecanismo inicial que ofrece Spring para definir beans es un fichero XML que, usualmente, se coloca en el classpath. Nosotros lo podemos incluir en una nueva carpeta de código, en la ruta `/src/main/resources`.

El fichero debe tener la siguiente estructura:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">
		
</beans>
```

### 2.1) Definición del bean (clase Java)

Podemos decir que un _bean_ es una clase Java _con esteroides_. Por tanto, lo primero que tenemos que hacer es crear la clase que va a definir nuestro bean. En muchos casos, es recomendable que esta clase implemente una interfaz que defina los métodos que va a implementar dicha clase.

Interfaz `ISaludador`:
```java
public interface ISaludador {
	
	public void setSaludo(String s);
	
	public void printSaludo();

}
```

Clase `MiPrimerBean`, que implementa la interfaz `ISaludador`:
```java
public class MiPrimerBean implements ISaludador {

	private String saludo;
	
	public void setSaludo(String s) {
		this.saludo = s;
	}
	
	public void printSaludo() {
		System.out.println(saludo);
	}
		
}
```

### 2.2) Creación del bean

Para crear un bean usando el mecanismo XML, tenemos que añadir un elemento de tipo `<bean>` en dicho fichero. Este quedaría de la siguiente forma:

```xml

	<bean id="saludator" 
		class="com.salesianostriana.dam.primerejemplospringcore.MiPrimerBean">
		<property name="saludo" value="¡Hola Mundo!" />		
	</bean>
		
```

Podemos destacar:

- `id`: identificador único del bean.
- `class`: tipo del bean (nombre cualificado, completo de la clase).

Además, para este bean se inyecta un valor, a través de la etiqueta `<property>`:

- `name`: nombre de la propiedad. Debe ser una propiedad de la clase sobre la que estamos definiendo el bean.
- `value`: valor con el que se _inicializa_.

## 3) Cómo _levantar_ el contenedor de inversión de control

Podemos imaginar el contenedor de inversión de control como _una especie de piscina donde están nadando los diferentes beans, esperando a que los pesquemos_.

Spring nos ofrece una serie de intefaces y clases para poder _levantar_ este contenedor de inversión de control. Si lo estamos definiendo a través de un fichero XML, lo podemos hacer así:

```java
public class App 
{
    public static void main( String[] args )
    {
    	// Creamos el contexto a través del fichero beans.xml
        ApplicationContext appContext = new ClassPathXmlApplicationContext("beans.xml");
        
        // Resto del código
        
        // Cerramos el contexto.
        ((ClassPathXmlApplicationContext) appContext).close();
    }
}

```

Una vez que se ejecute todo el código del proyecto, debemos finalizar convenientemente el contenedor de inversión de control. Se hace a través del método `close`.


## 4) Cómo obtener un bean

Para obtener un bean, lo podemos hacer a través del método `getBean`, con las diferentes implementaciones del mismo. Las más habituales:

- A través del `id`: 
```java
ISaludador comoMeDeLaGana = appContext.getBean(ISaludador.class);
```
- A través del tipo del bean: 
```java
MiPrimerBean otroBean = (MiPrimerBean) appContext.getBean("saludator");
```

## 5) ¿Qué es eso de singleton?

Por defecto, Spring configura los beans de modo _singleton_. Eso quiere decir que al crear el contexto, se instancia cada bean solamente una vez. Si a lo largo del programa tratamos de obtener diferentes referencias para dicho objeto, podemos observar que, en realidad, es el mismo objeto.
 
