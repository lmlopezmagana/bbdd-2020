
# Ejemplo 18 - Ejemplo de uso de Spring Security con herencia en el modelo de usuarios

Vamos a trabajar sobre el ejemplo anterior añadiendo dos elementos:

1. El modelo de seguridad tendrá herencia
2. En función del tipo concreto de usuario, queremos redirigir a URLs diferentes. Para implementar esto último, nos basaremos en la solución del siguiente [tutorial](http://websystique.com/spring-security/spring-security-4-role-based-login-example/). 

## Paso 1: Cambios en el modelo de usuario

Introducimos un cambios en el modelo de usuario: utilizaremos herencia de tipo _Joined_ y nuestra clase base será abstracta. 

La clase base, como decimos abstracta, quedaría de la siguiente forma:

```java
@Data @NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario implements UserDetails {
	
	
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
	
	public Usuario(String nombre, String apellidos, String email, String password) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
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

El interfaz `UserDetails` nos obliga a implementar una serie de métodos que indican si la cuenta de usuario no está bloqueada, está habilitado o sus credenciales no han expirado.

A partir de aquí, extendemos dicha clase en otras dos, `Empleado` y `Cliente`. En cada una de ellas implementamos el método abstracto de `Usuario`, que indica el rol o roles que tendrá cada clase.

```java
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class Empleado extends Usuario{
	
	private LocalDate fechaAltaSS;

	public Empleado(String nombre, String apellidos, String email, String password, 
			LocalDate fechaAltaSS) {
		super(nombre, apellidos, email, password);
		this.fechaAltaSS = fechaAltaSS;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

}

``` 

```java
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class Cliente extends Usuario {
	
	private LocalDate fechaNacimiento;

	public Cliente(String nombre, String apellidos, String email, String password, 
			LocalDate fechaNacimiento) {
		super(nombre, apellidos, email, password);
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
}

```

### Paso 1.1 Servicios y repositorios

En principio, no necesitamos crear para este ejemplo ni repositorios ni servicios de `Cliente` o `Empleado`, ya que podemos gestionar las inserciones o selecciones a través del servicio de `Usuario`. Si tuviéramos relaciones más complejas con otro tipo de entidades, posiblemente sea necesario crearlos.

## Paso 2. Cambios en nuestro servicio `UserDetailsService`

Dado que nuestra clase base `Usuario` implementa la interfaz `UserDetailsService`, **no tenemos que cambiar la logica de nuestro servicio `UserDetailsServiceImpl`**.

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


## Paso 3. Cambios en la seguridad

En el ejemplo anterior, teníamos el siguiente código en la clase `SecurityConfig`:

```java
	.formLogin()
		.loginPage("/login")
		.permitAll()
		.and()
```

Con este fragmento de código, le estamos indicando al sistema que si el login se produce con éxito, nos redirija a la página desde la cual se nos redirigió previamente al login, o en su defecto, a la raiz.

En este caso, **queremos tener redirecciones diferentes para roles diferentes**: el empleado, una vez logueado con éxito, accederá a la parte de administración, y el cliente, a la zona de clientes. ¿Cómo podemos conseguirlo? A la hora de configurar el formulario de login estamos usando (casi sin saberlo) una instancia de la clase `FormLoginConfigurer`, y uno de sus métodos (heredado de otra clase) es `successHandler`. Este método nos permite, en lugar de indicar una url de _éxito_, establecer dicha url a traves de un manejador. Dicho manejador debe ser una instancia de una clase que implemente la interfaz `AuthenticationSuccessHandler`. En lugar de implementar directamente la interfaz, podemos extender una clase, llamada `SimpleUrlAuthenticationSuccessHandler`, que ya implementa esa interfaz, y nos proporciona la funcionalidad básica de redirigir a una URL.

Nuestra configuración de seguridad podría quedar así:

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final CustomSuccessHandler customSuccessHandler;
	
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
		.antMatchers("/css/**","/js/**","/files/**", "/h2-console/**").permitAll()
			.antMatchers("/admin/**").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.successHandler(customSuccessHandler)
			.and()
		.logout()
			.logoutUrl("/logout")
			.permitAll()
			.and()
		.exceptionHandling()
			.accessDeniedPage("/acceso-denegado");
	
		// Añadimos esto para poder seguir accediendo a la consola de H2
		// con Spring Security habilitado.
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		// @formatter:on

	}

}

```

El manejador que nos permite redirigir a una u otra ruta en función del ROL sería este:

```java
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			System.out.println("Can't redirect");
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/*
	 * This method extracts the roles of currently logged-in user and returns
	 * appropriate URL according to his/her role.
	 */
	protected String determineTargetUrl(Authentication authentication) {
		String url = "";

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}

		if (isAdmin(roles)) {
			url = "/admin/";
		} else if (isUser(roles)) {
			url = "/";
		} else {
			url = "/acceso-denegado";
		}

		return url;
	}

	private boolean isUser(List<String> roles) {
		if (roles.contains("ROLE_USER")) {
			return true;
		}
		return false;
	}

	private boolean isAdmin(List<String> roles) {
		if (roles.contains("ROLE_ADMIN")) {
			return true;
		}
		return false;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}


```

La sistemática de esta clase no es complicada: una vez que el usuario se ha logueado correctamente, lo redirigimos a una URL; dicha URL se determina en función del rol del usuario (el usuario se nos presenta aquí a través de un objeto de tipo `Authentication`, un interfaz que forma parte de Spring Security es que es una forma básica de acceder a los datos del usuario que se ha autenticado).
