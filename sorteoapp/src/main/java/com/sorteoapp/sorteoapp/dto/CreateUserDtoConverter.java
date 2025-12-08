package com.sorteoapp.sorteoapp.dto;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUserDtoConverter {

	private final PasswordEncoder passwordEncoder;

	public UserEntity createUserToUserEntity(CreateUserDto newUser) {
		UserEntity userEntity = UserEntity.builder().name(newUser.getName()).username(newUser.getUsername())
				.firstName(newUser.getFirstName()).lastName(newUser.getLastName())
				.password(passwordEncoder.encode(newUser.getPassword())).email(newUser.getEmail())
				.avatar(newUser.getAvatar()).roles(Set.of(UserRole.USER)).build();

		return userEntity;
	}

}
