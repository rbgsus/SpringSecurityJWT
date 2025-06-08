package com.sorteoapp.sorteoapp.dto;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.UserEntity;

@Component
public class UserPerfilDtoConverter {

	public GetUserPerfilDto converterEntityTotUserPerfilDto(UserEntity user) {
		return GetUserPerfilDto.builder().dni(user.getDni()).name(user.getName()).username(user.getUsername())
				.firstName(user.getFirstName()).email(user.getEmail()).phone(user.getPhone())
				.fechaNacimiento(user.getFechaNacimiento()).avatar(user.getAvatar()).createdAt(user.getCreatedAt())
				.build();
	}
}
