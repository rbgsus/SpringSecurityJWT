package com.sorteoapp.sorteoapp.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipacionDto {

	private Long id;  // Identificador único de la participación

	private Long usuarioId;  // Id del usuario que realizó la compra

	private String usuarioName;  // Nombre o nick del usuario comprador

	private Long tarjetaId;  // Id de la tarjeta donde se compró el número

	private String tarjetaNombre;  // Nombre descriptivo de la tarjeta

	private String tarjetaDueño;  // Nick o username del dueño/creador de la tarjeta

	private Integer numero;  // Número comprado o seleccionado por el usuario

	private LocalDateTime fechaCompra;  // Fecha y hora en que se realizó la compra del número


}
