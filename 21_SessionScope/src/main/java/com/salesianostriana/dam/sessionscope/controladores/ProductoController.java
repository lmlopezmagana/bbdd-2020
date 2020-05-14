package com.salesianostriana.dam.sessionscope.controladores;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.salesianostriana.dam.sessionscope.carrito.Carrito;
import com.salesianostriana.dam.sessionscope.model.Producto;
import com.salesianostriana.dam.sessionscope.model.servicios.ProductoServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductoController {
	
	private final ProductoServicio productoServicio;
	private final Carrito carrito;
	
	@ModelAttribute("productos")
	public List<Producto> todosLosProductos() {
		return productoServicio.findAll();
	}
	
	@GetMapping({"/", "/list"})
	public String index(Model model) {
		return "index";
	}
	
	
	/**
	 * Métodos asociados al carrito
	 */
	@GetMapping("/carrito/add/{id}")
	public String addToCart(@PathVariable("id") Long id) {
		carrito.addToCarrito(id);	
		return "redirect:/carrito/mostrar";
	}

	@GetMapping("/carrito/mostrar")
	public String showCart(Model model) {
		model.addAttribute("carrito", carrito.getCarrito());
		model.addAttribute("numProductosDiferentes", carrito.numeroTotalProductosDiferentes());
		model.addAttribute("importeTotal", carrito.importeTotal());
		model.addAttribute("numUnidades", carrito.numeroTotalDeUnidades());
		return "carrito";
	}
	
	@GetMapping("/carrito/remove/{id}")
	public String removeFromCart(@PathVariable("id") Long id) {
		carrito.removeFromCarrito(id);
		return "redirect:/carrito/mostrar";
	}
	
	@GetMapping("/carrito/clear")
	public String clearCart() {
		carrito.clear();
		return "redirect:/";
	}
	
	
	@GetMapping("/carrito/process")
	public String procesarCarrito() {
		
		// En este método habría que conectar con otro servicio
		// para transformar los datos del carrito en una compra de
		// verdad. 
		
		// TODO Procesamiento de productos
		
		// Posteriormente, habría que vaciarlo y redirigir al usuario donde corresponda
		carrito.clear();
		return "redirect:/";
		
	}
	
	
}
