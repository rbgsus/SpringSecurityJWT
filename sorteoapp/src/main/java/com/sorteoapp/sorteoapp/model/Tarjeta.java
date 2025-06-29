package com.sorteoapp.sorteoapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
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
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	
	@OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Imagen> imagenes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	@JsonBackReference
	private UserEntity usuario;

}
