package com.salesianostriana.dam.primerproyectodatajpa.servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	
	private final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	// Este método sirve para simular el envío de un email.
	// En lugar de enviarlo, imprime todo por consola.
	public void send(String to, String subject, String body) {
		logger.info("Destinatario : " + to);
		logger.info("Asunto: " + subject);
		logger.info("Mensaje : " + body);
	}
	
	
}
