package com.sorteoapp.sorteoapp.dto;

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
	
	// TODO: Poner las categorias en el yml para poder añadir, modificar y eliminar
	private Categoria categoria;
}
