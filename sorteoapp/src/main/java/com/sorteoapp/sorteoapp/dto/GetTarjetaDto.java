package com.sorteoapp.sorteoapp.dto;

import java.util.List;

import com.sorteoapp.sorteoapp.model.Categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTarjetaDto {

	private Long idTarjeta;
	private String userName;
	private String nombreTarjeta;
	private String descripcion;
	private Double precio;
	private Categoria categoria;
	
	private List<GetImagenDto> imagenes;
}
