package com.sorteoapp.sorteoapp.dto;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.UserEntity;

@Component
public class UserDtoConverter {

	public GetuserDto converterEntityToGetUserDto(UserEntity user) {
		return GetuserDto.builder().username(user.getUsername()).avatar(user.getAvatar()).build();
	}
}
