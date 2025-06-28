package com.sorteoapp.sorteoapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.Imagen;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;

@Component
public class TarjetaDtoConverter {

	public GetTarjetaDto converterTarjetaToGetTarjetaDto(Tarjeta t) {
		List<GetImagenDto> imagenesDto = t.getImagenes() != null ? t.getImagenes().stream()
				.map(img -> GetImagenDto.builder().id(img.getId()).contenidoBase64(img.getContenidoBase64()).build())
				.collect(Collectors.toList()) : new ArrayList<>();

		return GetTarjetaDto.builder().idTarjeta(t.getIdTarjeta()).userName(t.getUsuario().getUsername())
				.nombreTarjeta(t.getNombreTarjeta()).descripcion(t.getDescripcion()).precio(t.getPrecio())
				.categoria(t.getCategoria()).imagenes(imagenesDto).build();
	}
	
	
	
	public Tarjeta converterCrearTarjetaDtoToTarjeta(CrearTarjetaDto dto, UserEntity usuario) {
		Tarjeta tarjeta = new Tarjeta();
		tarjeta.setNombreTarjeta(dto.getNombreTarjeta());
		tarjeta.setDescripcion(dto.getDescripcion());
		tarjeta.setPrecio(dto.getPrecio());
		tarjeta.setCategoria(dto.getCategoria());
		tarjeta.setUsuario(usuario);

		// Convertir imágenes base64 a entidades Imagen y asignarlas
		List<Imagen> imagenes = dto.getImagenesBase64().stream()
				.map(base64 -> {
					Imagen img = new Imagen();
					img.setContenidoBase64(base64);
					img.setTarjeta(tarjeta); // Importante para mantener la relación bidireccional
					return img;
				})
				.collect(Collectors.toList());

		tarjeta.setImagenes(imagenes);

		return tarjeta;
	}
}
