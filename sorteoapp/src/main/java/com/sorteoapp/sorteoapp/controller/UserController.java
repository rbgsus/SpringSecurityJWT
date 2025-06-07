package com.sorteoapp.sorteoapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.dto.EditPerfilUserDto;
import com.sorteoapp.sorteoapp.dto.GetUserPerfilDto;
import com.sorteoapp.sorteoapp.dto.GetuserDto;
import com.sorteoapp.sorteoapp.dto.UserDtoConverter;
import com.sorteoapp.sorteoapp.dto.UserPerfilDtoConverter;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.UserEntityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserEntityService userEntityService;

	private final UserDtoConverter userDtoConverter;

	private final UserPerfilDtoConverter userPerfilDtoConverter;

	// Endpoint para crear un usuario
	@PostMapping("/")
	public ResponseEntity<GetuserDto> createUser(@Valid @RequestBody CreateUserDto newUser) {
		// Llamada al servicio para registrar al nuevo usuario
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userDtoConverter.converterEntityToGetUserDto(userEntityService.registerUser(newUser)));
	}

	// Endpoint para obtener los datos del usuario autenticado
	@GetMapping("/me")
	public GetUserPerfilDto me(@AuthenticationPrincipal UserEntity userEntityloged) {
		return userPerfilDtoConverter.converterEntityTotUserPerfilDto(userEntityloged);
	}

	// Endpoint para obtener todos los usuarios
	@GetMapping("/all")
	public ResponseEntity<List<GetuserDto>> allUsuers() {
		List<GetuserDto> res = userEntityService.findAll().stream()
				.map(u -> userDtoConverter.converterEntityToGetUserDto(u)).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@PutMapping("/edit")
	public ResponseEntity<GetUserPerfilDto> editUserAsUser(@Valid @RequestBody EditPerfilUserDto editPerfilUserDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity usuario = (UserEntity) auth.getPrincipal();

		UserEntity actualizado = userEntityService.updateUserAsUser(usuario.getId(), editPerfilUserDto);

		GetUserPerfilDto perfilDto = userPerfilDtoConverter.converterEntityTotUserPerfilDto(actualizado);
		return ResponseEntity.ok(perfilDto);
	}

	/*
	 * Endpoint para eliminar un usuario por ID, ID que se obtendrá del propio token
	 * y que debo pensar si eliminar el usuario, borrar solo el dni y el username
	 * para que otro usuario pueda seleccionarlo , eliminar dni cambiando el nombre
	 * por 'Usuario eliminado' u otra opción ya que hay unas tarjeta o artículos
	 * asociados a un id y que hay que mantener un historial de ventas por cualquier
	 * problema que pueda ocurrir
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
		userEntityService.deleteUserById(id);
		return ResponseEntity.noContent().build(); // HTTP 204
	}

}
