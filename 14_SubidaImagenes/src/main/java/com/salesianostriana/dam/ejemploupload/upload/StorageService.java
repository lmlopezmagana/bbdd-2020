package com.salesianostriana.dam.ejemploupload.upload;

import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Este interfaz nos permite definir una abstracción de lo que debería
 * ser un almacén secundario de información, de forma que podamos usarlo
 * en un controlador.
 * 
 * De esta forma, vamos a poder utilizar un almacen que acceda a nuestro 
 * sistema de ficheros, o también podríamos implementar otro que estuviera
 * en un sistema remoto, almacenar los ficheros base64, en una base de datos...
 * 
 * 
 * @author Equipo de desarrollo de Spring
 *
 */
public interface StorageService<T, U> {

    void init();

    String store(MultipartFile file);

    Stream<T> loadAll();

    T load(U id);

    Resource loadAsResource(U id);
    
    void delete(U id);

    void deleteAll();

}