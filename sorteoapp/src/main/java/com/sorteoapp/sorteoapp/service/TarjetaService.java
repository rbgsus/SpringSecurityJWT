package com.sorteoapp.sorteoapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sorteoapp.sorteoapp.error.exceptions.TarjetaInvalidaException;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.repository.TarjetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class TarjetaService {

	private final TarjetaRepository tarjetaRepository;

	@Transactional
	public Tarjeta nuevaTarjeta(Tarjeta tarjeta) {
		if (tarjeta.getNombreTarjeta().isEmpty()) {
			throw new TarjetaInvalidaException("El nombre de la tarjeta no puede estar vacío");
		}

		// Guardar la tarjeta
		return tarjetaRepository.save(tarjeta);
	}

	@Transactional(readOnly = true)
	public List<Tarjeta> findAll() {
		return tarjetaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Tarjeta> findByUsuarioId(Long idUsuario) {
		return tarjetaRepository.findByUsuarioId(idUsuario);
	}

	public Page<Tarjeta> findAll(Pageable pageable) {
		return tarjetaRepository.findAll(pageable);
	}

}
