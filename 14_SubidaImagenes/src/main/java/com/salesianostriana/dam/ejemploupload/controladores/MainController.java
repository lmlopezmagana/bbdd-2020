package com.salesianostriana.dam.ejemploupload.controladores;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.salesianostriana.dam.ejemploupload.modelo.CosaConImagen;
import com.salesianostriana.dam.ejemploupload.servicios.CosaConImagenServicio;

@Controller
public class MainController {

	
	private final CosaConImagenServicio servicio;
	private final String BASE_IMAGE_PATH;
	
	
	public MainController(CosaConImagenServicio servicio, @Value("${image.base-path:/files}") String path) {
		this.servicio = servicio;
		this.BASE_IMAGE_PATH = path;
	}
	
	@ModelAttribute("base_image_path")
	public String baseImagePath() {
		return this.BASE_IMAGE_PATH;
	}
	
	
	@GetMapping("/")
	public String showForm(Model model) {
		model.addAttribute("cosa", new CosaConImagen());
		return "index";
	}
	
	
	@PostMapping("/submit")
	public String processForm(@ModelAttribute CosaConImagen cosaconImagen, MultipartFile file) {
		
		if (!file.isEmpty())
			servicio.save(cosaconImagen, file);
		else 
			servicio.save(cosaconImagen);
		
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("lista", servicio.findAll());
		return "list";
	}
	
	
}
