package com.sorteoapp.sorteoapp.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorteoapp.sorteoapp.model.UserRole;

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
public class AdminEditUserDto {

	private String dni;

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

	private String phone;

	@Builder.Default
	@JsonFormat(pattern = "yyyy-MM-dd") // Formato para LocalDate
	private LocalDate fechaNacimiento = LocalDate.now();

	private Set<UserRole> roles;

	private String avatar;

	private String password;
}
