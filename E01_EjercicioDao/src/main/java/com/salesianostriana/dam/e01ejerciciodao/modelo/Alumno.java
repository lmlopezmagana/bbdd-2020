package com.salesianostriana.dam.e01ejerciciodao.modelo;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Alumno {

	
	private long id;
	private String nombre, apellidos, email;
	private LocalDate fechaNacimiento;
	
	
	public int getEdad() {
		return Math.toIntExact(ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.of(Year.now().getValue(), 12, 31)));
	}
	
	
}
