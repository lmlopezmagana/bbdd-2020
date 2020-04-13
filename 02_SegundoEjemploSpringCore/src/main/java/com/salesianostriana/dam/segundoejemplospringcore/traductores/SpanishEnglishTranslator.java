package com.salesianostriana.dam.segundoejemplospringcore.traductores;

import org.springframework.stereotype.Service;

@Service
public class SpanishEnglishTranslator implements ITranslator {

	@Override
	public String translate(String message) {

		String result = null;

		if (!message.isEmpty()) {
			switch (message.toLowerCase()) {
			case "hola a todos los bicharracos!":
				result = "Hello to all the bicharracos!!!";
				break;
			default:
				result = "I don't understand you";
			}
		}

		return result;
	}

}
