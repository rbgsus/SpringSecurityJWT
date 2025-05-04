package com.sorteoapp.sorteoapp.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetuserDto {

	private LocalDate fecha;
	private String username;
	private String avatar;

	@NotBlank
	@Email(message = "El email no tiene formato válido")
	private String email;
	private Set<String> roles;

}
