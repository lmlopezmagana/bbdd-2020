
# Ejemplo 13 - Ejemplo de uso de Spring Security con servicio de autenticación  

Como aprendíamos en el [ejemplo anterior](https://github.com/lmlopezmagana/bbdd-2020/tree/master/12_SeguridadEnMemoria), el proceso de autenticación nos permite responder a la pregunta ¿y tú, quién eres?. En dicho ejemplo, hemos hecho la proceso  en memoria. Vamos a proceder ahora a crear un servicio de autenticación, que nos permitirá dar toda la versatilidad a dicho proceso.

## Modelos de autenticación

- _In Memory_: lo hemos aprendido en el ejemplo anterior.
- _JDBC_: los usuarios se almacenan en una base de datos relacional, accedida a través de Jdbc.
- _ldap_: los usuarios están en un almacén de seguridad, como por ejemplo de directorio activo de un servidor Windows.
- ***User Details Service***: se accede a la información de autenticación a través de un servicio. 

## Interfaces clave

Dentro del modelo de clases e interfaces de Spring Security, encontramos algunos de ellos que serán claves en el proceso de autenticación, como son:

- [`org.springframework.security.core.userdetails.UserDetails`](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/api/org/springframework/security/core/userdetails/UserDetails.html): **Proporciona información básica de un usuario**.
Las implementaciones no son utilizadas directamente por Spring Security por motivos de seguridad. Simplemente almacenan información de usuario que luego se encapsula en objectos de tipo `Authentication`. Esto permite separar  la información del usuario no relacionada con la seguridad (como direcciones de correo electrónico, números de teléfono, etc.). **Suele interesar implementar esta interfaz en lugar a usar directamente la clase `org.springframework.security.core.userdetails.User`**. 
- [`org.springframework.security.core.userdetails.UserDetailsService`](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/api/org/springframework/security/core/userdetails/UserDetailsService.html): interfaz principal que carga los datos de un usuario. Se utiliza en todo el framework como un DAO de usuarios. Solo proporciona un método, y este es de solo lectura. 
 - [`org.springframework.security.core.GrantedAuthority`](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/api/org/springframework/security/core/GrantedAuthority.html). Representa una autorización (un privilegio concreto) otorgado a un objeto de tipo `Authentication`.Ppodemos considerar a cada `GrantedAuthority` como un privilegio individual:  `READ_AUTHORITY`, `WRITE_PRIVILEGE` o incluso `CAN_EXECUTE_AS_ROOT`. _Lo importante a entender es que el nombre es **arbitrario**_. De manera similar, en Spring Security, podemos pensar en cada rol como una `GrantedAuthority` de _grano grueso_ que se representa como una cadena y tiene el prefijo "ROLE". 


## Paso 1: Nuestro modelo de usuario

En vista de lo que hemos leído en el apartado anterior, podemos plantear dos posibles soluciones para la implementación de nuestro modelo `Usuario`:

- Que nuestra propia clase `Usuario` implemente la interfaz `UserDetails`.
- A partir de nuestra clase `Usuario`, utilizar la clase `UserBuilder` para construir un objeto de tipo `UserDetails` en las circunstancias que sean necesarias.

**Escogemos la primera de ellas**.

Partimos de un diseño de clase de usuario básico:

```java
@Data @NoArgsConstructor
@Entity
public class Usuario implements UserDetails {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1409538586158223652L;

	@Id @GeneratedValue
	private Long id;
	
	private String nombre;
	private String apellidos;
	
	// Este será nuestro "username"
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private boolean admin;

	public Usuario(String nombre, String apellidos, String email, String password, boolean admin) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.admin = admin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role = "ROLE_";
		if (admin) {
			role += "ADMIN";
		} else {
			role += "USER";
		}
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	

}
``` 

Como es lógico, creamos un repositorio y servicio para esta entidad. Veamos el código, porque incluimos una consulta:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findFirstByEmail(String email);
	
	
}

```

```java
@Service
public class UsuarioServicio extends BaseService<Usuario, Long, UsuarioRepository>{

	public UsuarioServicio(UsuarioRepository repo) {
		super(repo);
	}
	
	public Optional<Usuario> buscarPorEmail(String email) {
		return repositorio.findFirstByEmail(email);
	}

}


```

Dicha consulta nos permitirá encontrar un usuario en base a su email, que será el campo que utilicemos en este ejemplo como _username_.

## Paso 2: Implementación de `UserDetailsService`

Creamos una nueva clase que implemente este interfaz:

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	
	private final UsuarioServicio usuarioServicio;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioServicio.buscarPorEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

}
```
El único método que nos obliga a implementar la interfaz es `loadByUsername`, un método que pueda buscar un usuario donde estén almacenados en base al campo _username_ (como decíamos antes, para nosotros, en este ejemplo, será el email). 

Aprovechando que hemos implementado la consulta, podemos devolver el usuario, si es que se encuentra, o lanzar una excepción de tipo `UsernameNotFoundException` en caso de que no.

## Paso 3: Actualización de la configuración de la seguridad

Vamos a cambiar nuestro método de configuración de autenticación para usar nuestro servicio `UserDetailsService`, así como para codificar las constraseñas. Para ello, usaremos un nuevo bean de tipo `BCryptPasswordEncoder`. También modificamos la autorización, permitiendo a todo el mundo el acceso al logout.

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		
		http
			.authorizeRequests()
				.antMatchers("/css/**", "/js/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/logout")
				.permitAll();
		
		
		// Añadimos esto para poder seguir accediendo a la consola de H2
		// con Spring Security habilitado.
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		// @formatter:on

	}

}

```

## Paso 4: Zona de administración

Creamos un controlador y una plantilla superbásicos en la ruta `/admin/`, para tener dos zonas separadas: para usuarios y para administradores.

```java
@Controller
@RequestMapping("/admin/")
public class AdminController {
	
	@GetMapping("/")
	public String index() {
		return "administrador/index";
	}

}

```

Creamos la plantilla en _templates/administrador/index.html_:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form th:action="@{/logout}" method="POST" id="logoutForm"></form>
	<h1>
		Bienvenido <span sec:authentication="name">Usuario</span> a la zona de administración.
	</h1>
	<a href="javascript:document.getElementById('logoutForm').submit()">Salir</a>

</body>
</html>
```


## Paso adicional: Datos de prueba

Creamos a traves de un bean de tipo `CommandLineRunner` algunos datos de prueba para poder loguearnos. ¡OJO! Hay que tener en cuenta antes de almacenar un usuario **tenemos que codificar su contraseña**.

```java
	@Bean
	public CommandLineRunner init(UsuarioServicio servicio, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			
			Usuario u = new Usuario();
			u.setAdmin(false);
			u.setNombre("Luis Miguel");
			u.setApellidos("López");
			u.setEmail("luismi.lopez@email.com");
			u.setPassword(passwordEncoder.encode("1234"));
			
			servicio.save(u);
			
			
			Usuario a = new Usuario();
			a.setAdmin(true);
			a.setNombre("Ángel");
			a.setApellidos("Narajo");
			a.setEmail("angel.naranjo@email.com");
			a.setPassword(passwordEncoder.encode("1234"));
			
			servicio.save(a);
			
		};
	}

```