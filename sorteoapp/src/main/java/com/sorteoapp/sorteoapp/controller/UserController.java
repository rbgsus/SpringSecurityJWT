package com.sorteoapp.sorteoapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.AdminEditUserDto;
import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.dto.EditUserDto;
import com.sorteoapp.sorteoapp.dto.GetuserDto;
import com.sorteoapp.sorteoapp.dto.UserDtoConverter;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.UserEntityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private UserDtoConverter userDtoConverter;

	// Endpoint para crear un usuario
	@PostMapping("/")
	public ResponseEntity<GetuserDto> createUser(@Valid @RequestBody CreateUserDto newUser) {
		// Llamada al servicio para registrar al nuevo usuario
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userDtoConverter.converterEntityToGetUserDto(userEntityService.registerUser(newUser)));
	}

	// Endpoint para obtener los datos del usuario autenticado
	@GetMapping("/me")
	public GetuserDto me(@AuthenticationPrincipal UserEntity userEntityloged) {
		return userDtoConverter.converterEntityToGetUserDto(userEntityloged);
	}

	// Endpoint para obtener todos los usuarios
	@GetMapping("/all")
	public ResponseEntity<List<GetuserDto>> allUsuers() {
		List<GetuserDto> res = userEntityService.findAll().stream()
				.map(u -> userDtoConverter.converterEntityToGetUserDto(u)).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	// Endpoint para obtener un usuario por su ID
	@GetMapping("/id/{id}")
	public ResponseEntity<GetuserDto> getUserByIdDto(@PathVariable Long id) {
		UserEntity user = userEntityService.findByIdOrThrow(id);
		GetuserDto userDto = userDtoConverter.converterEntityToGetUserDto(user);
		return ResponseEntity.ok(userDto);
	}

	// Endpoint para editar un usuario como "guest"
	@PutMapping("/edit/{id}")
	public ResponseEntity<UserEntity> editUserGuest(@PathVariable Long id,
			@Valid @RequestBody EditUserDto editUserDto) {
		UserEntity actualizado = userEntityService.updateUserAsGuest(id, editUserDto);
		return ResponseEntity.ok(actualizado);
	}

	// Endpoint para editar un usuario como "admin"
	@PutMapping("/admin/edit/{id}")
	public ResponseEntity<UserEntity> editUserAdmin(@PathVariable Long id,
			@Valid @RequestBody AdminEditUserDto adminEditUserDto) {
		UserEntity actualizado = userEntityService.updateUserAsAdmin(id, adminEditUserDto);
		return ResponseEntity.ok(actualizado);
	}

	// Endpoint para eliminar un usuario por ID (solo ADMIN)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
		userEntityService.deleteUserById(id);
		return ResponseEntity.noContent().build(); // HTTP 204
	}

}
