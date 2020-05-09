# Bases de datos - 2019/20
Repositorio del módulo de Bases de Datos para el curso 2019-20 (DAM - Salesianos Triana)

A continuación, se detallan los proyectos que se incluyen dentro del repositorio.

## Ejemplos iniciales

* [01_PrimerEjemploSpringCore](https://github.com/lmlopezmagana/bbdd-2020/tree/master/01_PrimerEjemploSpringCore): se trata de un sencillo ejemplo de integración de Spring en un proyecto Maven. Se utiliza la configuración a través de un fichero XML.
* [02_SegundoEjemploSpringCore](https://github.com/lmlopezmagana/bbdd-2020/tree/master/02_SegundoEjemploSpringCore): este segundo ejemplo muestra como realizar la configuración de beans a través de anotaciones.
* [03_PrimerProyectoSpringBoot](https://github.com/lmlopezmagana/bbdd-2020/tree/master/03_PrimerProyectoSpringBoot): ejemplo de un primer proyecto creado con Spring Boot.
* [04_PrimerProyectoWeb](https://github.com/lmlopezmagana/bbdd-2020/tree/master/04_PrimerProyectoWeb): en este caso, se trata de un proyecto similar al anterior, pero en el que creamos un primer controlador y una plantilla.
* [05_PrimerProyectoDataJpa](https://github.com/lmlopezmagana/bbdd-2020/tree/master/05_PrimerProyectoDataJpa): ejemplo de creación de un proyecto desde cero que incluye Spring Data JPA y la base de datos H2. También se puede ver la creación de una entidad, un repositorio, y un ejemplo de su uso.

## Manejo de asociaciones

* [06_ManyToOne](https://github.com/lmlopezmagana/bbdd-2020/tree/master/06_ManyToOne): en este ejemplo se trabajan dos conceptos
    * La creación de un _servicio base_, que nos permite encapsular el repositorio, evitando así el acomplamiento (dista de ser una solución ideal, pero es un buen comienzo). Los demás servicios que sirvan para acceder a los datos pueden extender este.
    * El ejemplo de una asociación muchos-a-uno (`@ManyToOne`) que es tratada unidireccionalmente.
* [07_OneToManyBidi](https://github.com/lmlopezmagana/bbdd-2020/tree/master/07_OneToManyBidi): ejemplo que completa el anterior, para dar un tratamiento bidireccional a una asociación one-to-many <- -> many-to-one
* [08_OneToManyComposicion](https://github.com/lmlopezmagana/bbdd-2020/tree/master/08_OneToManyComposicion): ejemplo alternativo al anterior, para dar un tratamiento bidireccional a una asociación _one-to_many_ que en este caso es de composición. Tratamos el lado compuesto como entidad fuerte, y el componente como débil.
* [09_ManyToManyBidi](https://github.com/lmlopezmagana/bbdd-2020/tree/master/09_ManyToManyBidi): ejemplo de implementación de una asociación _many-to-many_ bidireccional.
* [10_ManyToManyBidiLogicaNegocio](https://github.com/lmlopezmagana/bbdd-2020/tree/master/10_ManyToManyBidiLogicaNegocio): ejemplo de implementación de algo de lógica de negocio sobre el ejemplo anterior.
* [11_ManyToManyExtra](https://github.com/lmlopezmagana/bbdd-2020/tree/master/11_ManyToManyExtra): ejemplo de implementación de una asociación _many-to-many_ con atributos extra.

## Seguridad

* [12_SeguridadEnMemoria](https://github.com/lmlopezmagana/bbdd-2020/tree/master/12_SeguridadEnMemoria): ejemplo inicial de integración de Spring Security, utilizando usuarios que están en memoria.
* [13_SeguridadConUDS](https://github.com/lmlopezmagana/bbdd-2020/tree/master/13_SeguridadConUDS): ejemplo más completo, en el que los usuarios se almacenan en la base de datos a través de un servicio.
* [18_SeguridadConHerencia](https://github.com/lmlopezmagana/bbdd-2020/tree/master/18_SeguridadConHerencia): ejemplo basado en el anterior, pero que hace uso de herencia para tener diferentes clases de tipo usuario, realizando además una redirección específica tras el login basada en el rol del usuario.

## Subida de imágenes

* [14_SubidaImagenes](https://github.com/lmlopezmagana/bbdd-2020/tree/master/14_SubidaImagenes): ejemplo con todo el código neceasario para integrar en vuestros proyectos para poder implementar la subida de imágenes. **Dadas la circunstancias de este curso, este ejemplo es más recomendable para utilizar que el siguiente**.
* [Upload Files](https://spring.io/guides/gs/uploading-files/): ejemplo oficial de Spring, en el cual se basa el anterior, con el código necesario para realizar la subida de ficheros hacia el sistema de ficheros del proyecto.


## Herencia

* [15_Herencia_MappedSuperClass](https://github.com/lmlopezmagana/bbdd-2020/tree/master/15_Herencia_MappedSuperclass): ejemplo de implementación de herencia con Spring Data JPA donde la clase base no es una entidad.
* [16_SingleTable](https://github.com/lmlopezmagana/bbdd-2020/tree/master/16_Herencia_SingleTable): estrategia de herencia en la que tanto la clase base como las hijas se trasladan a una sola tabla y se utiliza una columna como discriminante.
* [17_Herencia_Joined](https://github.com/lmlopezmagana/bbdd-2020/tree/master/17_Herencia_Joined): última estrategia de herencia, en la cual se genera una tabla para la clase base, con todos sus atributos; y también una tabla para cada una de las subclases, que incluirá una clave externa a la tabla de la clase base, así como los posibles atributos propios que tenga.

## Consultas

## Soluciones a ejercicios    

Además, puedes encontrar algunos proyectos que comienzan por _EXX_, que significa que son la solución a algún ejercicio.

* [E01_EjercicioDao](https://github.com/lmlopezmagana/bbdd-2020/tree/master/E01_EjercicioDao): solución al ejercicio de elaboración de un proyecto que implementa el patrón DAO, así como un servicio que interactúa con el mismo.
* [E01_EjercicioDao_SpringBoot](https://github.com/lmlopezmagana/bbdd-2020/tree/master/E01_EjercicioDao_SpringBoot): solución al mismo ejercicio, pero esta vez usando Spring Boot.
