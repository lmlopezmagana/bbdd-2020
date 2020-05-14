package com.salesianostriana.dam.sessionscope.carrito;

import java.util.Map;

import com.salesianostriana.dam.sessionscope.model.Producto;

public interface Carrito {

	/**
	 * Añade un producto al carrito. 
	 * Si el producto ya estaba, incrementa en una unidad el nº de elementos
	 * Si no existe previamente, lo inserta con una unidad
	 * @param p
	 */
	void addToCarrito(Producto p);

	void addToCarrito(long id);

	/**
	 *  Eliminar el producto del carrito
	 *  @param id
	 */
	void removeFromCarrito(Long id);

	void removeFromCarrito(Producto p);
	
	
	/**
	 * Vaciar carrito de productos
	 */
	void clear();

	/**
	 * Devuelve los productos del carrito en un Map que
	 * no se puede modificar
	 * NO QUEREMOS QUE NADIE CAMBIE ESTA COLECCIÓN
	 * DESDE FUERA DE LA CLASE; QUEREMOS QUE LO HAGA
	 * A TRAVÉS DE LOS MÉTODOS addToCarrito o removeFromCarrito
	 * @return
	 */
	Map<Producto, Integer> getCarrito();

	/**
	 * Número de productos diferentes en el carrito
	 * @return
	 */
	int numeroTotalProductosDiferentes();

	/**
	 * Número de unidades de productos en el carrito
	 * Si del producto P1 solicito una unidad, y de P2 solicito dos unidades
	 * este método debe devolver 3
	 * @return
	 */
	int numeroTotalDeUnidades();

	/**
	 * Importe total de lo que se incluye en el carrito.
	 * 
	 * @return
	 */
	float importeTotal();

}