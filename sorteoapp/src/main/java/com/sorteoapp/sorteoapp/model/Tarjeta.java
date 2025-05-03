package com.sorteoapp.sorteoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Tarjeta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTarjeta;

	@NotBlank(message = "El nombre no puede estar vacío")
	private String nombreTarjeta;
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@Positive(message = "El precio debe ser mayor a 0")
	private Double precio;

	// Lista de roles del usuario. Se guarda como conjunto de strings
	// Se inicializa en vacío
	@Enumerated(EnumType.STRING) // Se guarda como String en la BD
	private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private UserEntity usuario;

}
