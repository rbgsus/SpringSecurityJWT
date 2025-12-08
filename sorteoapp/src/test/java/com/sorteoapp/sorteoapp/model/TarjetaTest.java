package com.sorteoapp.sorteoapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TarjetaTest {

	@Test
	void crearTarjetaOk() {

		Tarjeta t = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("Descripción").build();

		assertEquals("play", t.getNombreTarjeta());
		assertEquals(20.0, t.getPrecio());
		assertEquals(Categoria.TECNOLOGÍA, t.getCategoria());
		assertEquals("Descripción", t.getDescripcion());

	}

	@Test
	void realacionUserOk() {
		UserEntity user = new UserEntity();

		Tarjeta t = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("Descripción").usuario(user).build();

		assertEquals(user, t.getUsuario());

	}

	@Test
	void imagenTarjeta() {
		Imagen i = new Imagen();
		List<Imagen> r = List.of(i);
		Tarjeta t = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("Descripción").imagenes(r).build();
		assertEquals(r, t.getImagenes());

	}

}
