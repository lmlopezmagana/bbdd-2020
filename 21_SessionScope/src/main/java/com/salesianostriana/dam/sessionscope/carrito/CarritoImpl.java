package com.salesianostriana.dam.sessionscope.carrito;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.salesianostriana.dam.sessionscope.model.Producto;
import com.salesianostriana.dam.sessionscope.model.servicios.ProductoServicio;

import lombok.RequiredArgsConstructor;

@Service
@SessionScope
@RequiredArgsConstructor
public class CarritoImpl implements Carrito {
	
	
	private Map<Producto, Integer> productosEnCarrito = new LinkedHashMap<>();
	private final ProductoServicio productoServicio;
	
		
	/**
	 * Añade un producto al carrito. 
	 * Si el producto ya estaba, incrementa en una unidad el nº de elementos
	 * Si no existe previamente, lo inserta con una unidad
	 * @param p
	 */
	@Override
	public void addToCarrito(Producto p) {
		if (productosEnCarrito.containsKey(p)) {
			productosEnCarrito.put(p, productosEnCarrito.get(p)+1);
		} else {
			productosEnCarrito.put(p, 1);
		}
	}
	
	@Override
	public void addToCarrito(long id) {
		Producto producto = productoServicio.findById(id);
		if (producto != null) {
			addToCarrito(producto);
		}
	}
	
	/**
	 *  Eliminar el producto del carrito
	 *  @param id
	 */
	@Override
	public void removeFromCarrito(Long id) {
		Producto producto = productoServicio.findById(id);
		if (producto != null) {
			removeFromCarrito(producto);
		}
	}
	
	@Override
	public void removeFromCarrito(Producto p) {
		productosEnCarrito.remove(p);
	}
	
	/**
	 * Devuelve los productos del carrito en un Map que
	 * no se puede modificar
	 * NO QUEREMOS QUE NADIE CAMBIE ESTA COLECCIÓN
	 * DESDE FUERA DE LA CLASE; QUEREMOS QUE LO HAGA
	 * A TRAVÉS DE LOS MÉTODOS addToCarrito o removeFromCarrito
	 * @return
	 */
	@Override
	public Map<Producto, Integer> getCarrito() {
		return Collections.unmodifiableMap(productosEnCarrito);
	}
	
	/**
	 * Número de productos diferentes en el carrito
	 * @return
	 */
	@Override
	public int numeroTotalProductosDiferentes() {
		return productosEnCarrito.size();
	}
	
	/**
	 * Número de unidades de productos en el carrito
	 * Si del producto P1 solicito una unidad, y de P2 solicito dos unidades
	 * este método debe devolver 3
	 * @return
	 */
	@Override
	public int numeroTotalDeUnidades() {
		int acumulador = 0;
		for(Producto p : productosEnCarrito.keySet()) {
			acumulador += productosEnCarrito.get(p);
		}
		return acumulador;
	}
	
	/**
	 * Importe total de lo que se incluye en el carrito.
	 * 
	 * @return
	 */
	@Override
	public float importeTotal() {
		float acumulador = 0.0f;
		for(Producto p : productosEnCarrito.keySet()) {
			acumulador += productosEnCarrito.get(p) * p.getPrecio();
		}
		return acumulador;
		
	}

	@Override
	public void clear() {
		productosEnCarrito.clear();
		
	}
	

}
