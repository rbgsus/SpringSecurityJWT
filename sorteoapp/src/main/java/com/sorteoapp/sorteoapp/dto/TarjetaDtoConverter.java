package com.sorteoapp.sorteoapp.dto;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.Tarjeta;

@Component
public class TarjetaDtoConverter {

	public GetTarjetaDto converterTarjetaToGetTarjetaDto(Tarjeta t) {
		return GetTarjetaDto.builder().idUsuario(t.getUsuario().getId()).idTarjeta(t.getIdTarjeta()).nombreTarjeta(t.getNombreTarjeta())
				.descripcion(t.getDescripcion()).precio(t.getPrecio()).categoria(t.getCategoria()).build();
	}
}
