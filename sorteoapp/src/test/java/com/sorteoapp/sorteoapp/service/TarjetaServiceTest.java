package com.sorteoapp.sorteoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sorteoapp.sorteoapp.error.exceptions.TarjetaInvalidaException;
import com.sorteoapp.sorteoapp.model.Categoria;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.repository.TarjetaRepository;

public class TarjetaServiceTest {

	@Mock
	TarjetaRepository tarjetaRepository;

	@InjectMocks
	TarjetaService tarjetaService;

	public TarjetaServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void crearTarjeta() {
		Tarjeta t = Tarjeta.builder().nombreTarjeta("Play").precio(20.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();
		when(tarjetaRepository.save(t)).thenReturn(t);
	}

	@Test
	void crearTarjetaExceptionNameEmpty() {
		Tarjeta t = Tarjeta.builder().nombreTarjeta("").precio(20.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();

		assertThrows(TarjetaInvalidaException.class, () -> {
			tarjetaService.nuevaTarjeta(t);
		});

		TarjetaInvalidaException exception = assertThrows(TarjetaInvalidaException.class,
				() -> tarjetaService.nuevaTarjeta(t));

		assertEquals("El nombre de la tarjeta no puede estar vacío", exception.getMessage());

	}

	@Test
	void findAllCards() {
		Tarjeta t1 = Tarjeta.builder().nombreTarjeta("").precio(20.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();
		Tarjeta t2 = Tarjeta.builder().nombreTarjeta("").precio(40.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();

		List<Tarjeta> l = List.of(t1, t2);

		when(tarjetaRepository.findAll()).thenReturn(l);

		List<Tarjeta> res = tarjetaService.findAll();

		assertEquals(2, res.size());
		verify(tarjetaRepository, times(1)).findAll();

	}

	@Test
	void findByUserIdCards() {
		Tarjeta t1 = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("").build();
		Tarjeta t2 = Tarjeta.builder().nombreTarjeta("wii").precio(40.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();

		List<Tarjeta> l = List.of(t1, t2);

		when(tarjetaRepository.findByUsuarioId(1L)).thenReturn(l);

		List<Tarjeta> res = tarjetaService.findByUsuarioId(1L);

		assertEquals(2, res.size());
		verify(tarjetaRepository, times(1)).findByUsuarioId(1L);
		assertEquals("play", res.get(0).getNombreTarjeta());
		assertEquals("wii", res.get(1).getNombreTarjeta());

	}

	@Test
	void saveTarjeta() {
		Tarjeta t1 = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("").build();

		when(tarjetaRepository.save(t1)).thenReturn(t1);

		Tarjeta res = tarjetaService.nuevaTarjeta(t1);

		verify(tarjetaRepository, times(1)).save(t1);
		assertEquals(t1, res);

	}

	@Test
	void pageTarjetas() {
		Tarjeta t1 = Tarjeta.builder().nombreTarjeta("play").precio(20.0).categoria(Categoria.TECNOLOGÍA)
				.descripcion("").build();

		Tarjeta t2 = Tarjeta.builder().nombreTarjeta("wii").precio(20.0).categoria(Categoria.TECNOLOGÍA).descripcion("")
				.build();

		List<Tarjeta> l = List.of(t1, t2);

		Page<Tarjeta> page = new PageImpl<>(l);

		Pageable p = PageRequest.of(0, 10);

		when(tarjetaRepository.findAll(p)).thenReturn(page);

		Page<Tarjeta> res = tarjetaService.findAll(p);

		assertEquals(2, res.getContent().size());
		assertEquals("play", res.getContent().get(0).getNombreTarjeta());
		assertEquals("wii", res.getContent().get(1).getNombreTarjeta());

	}


	

}
