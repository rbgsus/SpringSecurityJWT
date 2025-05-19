package com.sorteoapp.sorteoapp.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {

	// Creación de usuario con los campos obligatorios de UserEntity
	@NotBlank(message = "El campo del nombre no puede estar vacío")
	@NotEmpty(message = "El campo del nombre no puede estar vacío")
	private String name;

	@NotBlank(message = "El campo del nombre de usuario nick no puede estar vacío")
	@NotEmpty(message = "El campo del nombre de usuario nick no puede estar vacío")
	private String username;

	@NotBlank(message = "El campo del primer apellido no puede estar vacío")
	@NotEmpty(message = "El campo del primer apellido no puede estar vacío")
	private String firstName;
	
	private String lastName;

	@NotBlank(message = "El campo del email no puede estar vacío")
	@NotEmpty(message = "El campo del email no puede estar vacío")
	@Email(message = "El email no está correctamente formado")
	private String email;

	@NotBlank(message = "El campo de la contraseña no puede estar vacío")
	@NotEmpty(message = "El campo de la contraseña no puede estar vacío")
	private String password;

	@NotBlank(message = "El campo de la contraseña no puede estar vacío")
	@NotEmpty(message = "El campo de la contraseña no puede estar vacío")
	private String password2;

	@Builder.Default
	@JsonFormat(pattern = "yyyy-MM-dd") // Formato para LocalDate
	private LocalDate fechaNacimiento = LocalDate.now();
	
	private String avatar;

}
