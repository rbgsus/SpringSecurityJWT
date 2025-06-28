package com.sorteoapp.sorteoapp.service;

import java.util.List;

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
public class TarjetaService extends BaseService<Tarjeta, Long, TarjetaRepository> {

	@Transactional
	public Tarjeta nuevaTarjeta(Tarjeta tarjeta) {
		if (tarjeta.getNombreTarjeta().isEmpty()) {
			throw new TarjetaInvalidaException("El nombre de la tarjeta no puede estar vacío");
		}

		// Guardar la tarjeta
		return save(tarjeta);
	}

	@Transactional(readOnly = true)
	public List<Tarjeta> findAll() {
		return super.findAll();
	}
	
    @Transactional(readOnly = true)
	public List<Tarjeta> findByUsuarioId(Long idUsuario) {
	    return repositorio.findByUsuarioId(idUsuario);
	}

}
