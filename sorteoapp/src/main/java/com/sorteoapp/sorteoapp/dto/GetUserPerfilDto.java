package com.sorteoapp.sorteoapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetUserPerfilDto {

	private String dni;
	private String name;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private LocalDate fechaNacimiento;
	private String avatar;
	private LocalDateTime createdAt;
}
