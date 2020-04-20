package com.salesianostriana.dam.primerproyectoweb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("mensaje", "Hola Mundo");
		return "index";
	}
	

}
