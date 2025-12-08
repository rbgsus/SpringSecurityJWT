package com.sorteoapp.sorteoapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ImagenTest {

	@Test
	void imagen() {
		Tarjeta t = new Tarjeta();

		Imagen i = Imagen.builder().id(1L).contenidoBase64("").tarjeta(t).build();

		assertEquals(1L, i.getId());
		assertEquals("", i.getContenidoBase64());
		assertEquals(t, i.getTarjeta());

	}

}
