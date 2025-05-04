package com.sorteoapp.sorteoapp.dto;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;

@Component
public class UserDtoConverter {

	public GetuserDto converterEntityToGetUserDto(UserEntity user) {
		return GetuserDto.builder().username(user.getUsername()).avatar(user.getAvatar()).email(user.getEmail())
				.roles(Set.of(UserRole.USER.name())).build();
	}
}
