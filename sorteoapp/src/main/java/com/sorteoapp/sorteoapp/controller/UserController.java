package com.sorteoapp.sorteoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.dto.GetuserDto;
import com.sorteoapp.sorteoapp.dto.UserDtoConverter;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.UserEntityService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private UserDtoConverter userDtoConverter;

	// Endpoint para crear un usuario
	@PostMapping("/")
	public ResponseEntity<GetuserDto> createUser(@RequestBody CreateUserDto newUser) {

		// Si el usuario es creado correctamente, se devuelve una respuesta 201
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userDtoConverter.converterEntityToGetUserDto(userEntityService.nuevoUsuario(newUser)));
	}

	@GetMapping("/me")
	public GetuserDto me(@AuthenticationPrincipal UserEntity userEntityloged) {
		return userDtoConverter.converterEntityToGetUserDto(userEntityloged);

	}

}
