package com.sorteoapp.sorteoapp.dto;

import java.util.List;

import com.sorteoapp.sorteoapp.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearTarjetaDto {

	@NotBlank(message = "El nombre no puede estar vacío")
	private String nombreTarjeta;

	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@Positive(message = "El precio debe ser mayor a 0")
	private Double precio;

	@NotNull(message = "La categoría es obligatoria")
	private Categoria categoria;

	// Lista de imágenes en base64
	@NotNull(message = "Debes enviar al menos una imagen")
	private List<String> imagenesBase64;
}
