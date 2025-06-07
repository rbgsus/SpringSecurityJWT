package com.sorteoapp.sorteoapp.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sorteoapp.sorteoapp.error.exceptions.TarjetaInvalidaException;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.repository.TarjetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class TarjetaService extends BaseService<Tarjeta, Long, TarjetaRepository> {

	public Tarjeta nuevaTarjeta(Tarjeta tarjeta) {
		if (tarjeta.getNombreTarjeta().isEmpty()) {
			throw new TarjetaInvalidaException("El nombre de la tarjeta no puede estar vacío");
		}

		// Guardar la tarjeta
		return save(tarjeta);
	}

}
