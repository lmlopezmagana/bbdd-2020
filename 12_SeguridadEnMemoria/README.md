
# Ejemplo 12 - Ejemplo mínimo de uso de Spring Security 

Spring Security es un potente y altamente personalizable framework de autenticación y control de acceso. Es el estándar de facto para asegurar las aplicaciones basadas en Spring.

Spring Security es un framework que se enfoca en proporcionar tanto autenticación como autorización para aplicaciones Java. Al igual que todos los proyectos de Spring, el verdadero poder de Spring Security se encuentra en la facilidad con que se puede extender para cumplir con los requisitos personalizados.

En las versiones anteriores a Spring Boot, la configuración e integración con Spring Web MVC era ciertamente farragosa, pero ahora es prácticamente directa, y tan solo tenemos que centrarnos en los aspectos específicos de nuestra app.

## Características

- Soporte completo y extensible para autenticación y autorización
- Protección contra ataques como la fijación de sesiones, el _clickjacking_, la falsificación de solicitudes entre sitios, etc.
- Integración de la API Servlet
- Integración opcional con Spring Web MVC
- ...

## Seguridad: autenticación y autorización

- La **autenticación** es el proceso por el cual se identifica un cliente (persona) como válida para posteriormente acceder a ciertos recursos definidos.
- La **autorización** es el proceso sobre el cual se establecen qué tipos de recursos están permitidos o denegados para cierto usuario o grupo de usuarios concreto.

## Spring Security en nuestro `pom.xml`

Para añadir Spring Security a nuestro proyecto, tenemos una dependencia _starter_ disponible:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## ¿Qué vamos a aprender en este proyecto?

1. Autenticación en memoria
2. Autorización básica de una aplicación
3. Formulario de login personalizado

## Paso 1: Creación de la clase de seguridad

1. Vamos a añadir un nuevo paquete para `seguridad`.
2. Creamos una nueva clase que
	1. Extiende a `WebSecurityConfigurerAdapter`
	2. Anotada con `@EnableWebSecurity`
	3. También anotada con `@Configuration`

**¿Qué conseguimos con esto?** Esto nos permitirá configurar la seguridad de nuestra aplicación tan solo sobrescribiendo unos métodos.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

}
```

## Paso 2: Creación del método que configura la **autenticación**

En este primer ejemplo lo haremos en memoria. En los siguientes podremos hacerlo en una base de datos. Tampoco codificaremos las contraseñas por ahora.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
			.withUser("admin")
			.password("{noop}admin")
			.roles("ADMIN");
	}
	
}
``` 

> **¿Qué tenemos ya con este código?** Solo con este código ya tenemos: requisito de autenticación en todas las URLs, generación de un formulario de login, permitir el acceso a un usuario con _Username_ admin y _Password_ admin, permitir hacer logout, prevención de ataques CSRF, entre otras (_Session Fixation_, _X-XSS-Protection_, _Clickjacking_, …), integración con métodos del api _Servlet_ (`HttpServletRequest`)

> La clase de configuración ha registrado un filtro de Servlet especial, llamado `springSecurityFilterChain`. Es una cadena de filtros responsable de toda la seguridad (proteger las URLs, validar usuario y contraseña, redirigir al formulario de login, …)

## Paso 3: Configuración de la autorización

A través de este método vamos a configurar a qué sí tiene acceso el usuario y a qué no. Lo hacemos a través de un objeto `HttpSecurity`:

```java
@Override
protected void configure(HttpSecurity http) throws Exception {

}
```

### Paso 3.1: Método de login

Vamos a modificar lo necesario para cambiar el formulario de login por defecto por uno propio, además de cambiar la ruta a `/login`. Además, tenemos que permitir que cualquier usuario pueda acceder al formulario de login.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll();

}
```

### Paso 3.2: Customizar el formulario de login

Para _customizar_ el formulario necesitaríamos un controlador que atienda la petición de login y nos derive a su plantilla. 

> Si un controlador solamente sirve para llevarnos a una plantilla, lo podemos simplificar.

Creamos una nueva clase que:

1. Estará en un nuevo paquete, llamado `configuracion`.
2. Esté anotada con `@Configuration`
3. Implementa la interfaz `WebMvcConfigurer`.
4. Sobrescriba el método `addViewControllers`

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login");
	}
	
}
```

### Paso 3.3: Plantilla del formulario de login

Creamos una nueva plantilla, llamada `login.html`. Seguro que cualquier de vosotros es capaz de darle estilo a esta plantilla, o utilizar algún framework tipo _bootstrap_.

**Importante**. No debemos olvidar que, con la configuración actual:

- debemos configurar la acción del formulario con `th:action="@{/login}"`.
- los campos para el usuario y contraseña en el formulario deben llamarse `username` y `password`.

```html
<form method="POST" action="#" th:action="@{/login}">
	<input type="text" id="username" name="username" placeholder="Username" required autofocus> 
	<input type="password" name="password" id="password" placeholder="Password" required>
	<input type="submit" name="enviar" value="Enviar" />
</form>
```

### Paso 3.4: Permitir acceso a recursos estáticos

Nuestra seguridad es tan restrictiva que no permite el acceso a estilos css, código javascript, ... Para poder _servir_ dicho contenido, tenemos que permitir el acceso a dichas rutas. Lo hacemos mediante el método `antMatchers(...)`, que recibe un _varargs_ de `String` (podemos usar el formato _glob_).

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()
			.antMatchers("/css/**","/js/**","/webjars/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll();
	
}
```

## Paso 4: Logout

Permitimos que el usuario autenticado realice el _logout_ para poder invalidar la sesión y limpiar el contexto de seguridad. Además, lo redirigiremos de nuevo al login.

**¡OJO! Para poder mantener la seguridad CSRF, necesitamos que la petición para realizar el logout sea una petición POST.** Tenemos varias formas de hacer esto, aunque la más sencilla es a través de un formulario.

Para poder ilustrar el funcionamiento del logout, creamos también una plantilla de bienvenida, a la cual accedemos una vez que se haya completado el login correctamente.


## Paso 5: Mostrar el nombre del usuario logueado en la plantilla

Tenemos varios mecanismos para realizar esto:

- A través del objeto `SecurityContextHolder` (podríamos decir que esto es el "estilo Java").
- **Usando libería extra de seguridad para Thymeleaf**.

### Paso 5.1: Añadimos la dependencia al `pom.xml`

```xml
<dependency>
	<groupId>org.thymeleaf.extras</groupId>
	<artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```

### Paso 5.2: Añadimos un nuevo espacio de nombres a la plantilla

```html
<html 
	xmlns:th="http://www.thymeleaf.org" 
	xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```

### Paso 5.3: Utilizamos alguno de los métodos de esta plantilla para escribir el nombre

```html
<h1>Bienvenido <span sec:authentication="name">Usuario</span></h1>
```

El código completo necesarío sería:

- El controlador (básico):

```java
@Controller
public class WelcomeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
}
```

- la plantilla:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" 
xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
<meta charset="UTF-8">
<title>¡Bienvenido!</title>
</head>
<body>
	<form th:action="@{/logout}" method="POST" id="logoutForm"></form>
	<h1>Bienvenido <span sec:authentication="name">Usuario</span></h1>
	<a href="javascript:document.getElementById('logoutForm').submit()">Salir</a>
</body>
</html>
```
