package com.sorteoapp.sorteoapp.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;
import com.sorteoapp.sorteoapp.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, Long, UserEntityRepository> {

	private final PasswordEncoder passwordEncoder;

	public Optional<UserEntity> findUserByUsername(String username) {
		return this.repositorio.findByUsername(username);
	}

	public UserEntity nuevoUsuario(CreateUserDto newUser) {
		// Verificar si el username ya existe
		if (findUserByUsername(newUser.getUsername()).isPresent()) {
			throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
		}

		// Verificar que las contraseñas coincidan
		if (!newUser.getPassword().equals(newUser.getPassword2())) {
			throw new NewUserWithDifferentPasswordsException("Las contraseñas no coinciden");
		}

		// Crear entidad de usuario
		UserEntity userEntity = UserEntity.builder().username(newUser.getUsername())
				.password(passwordEncoder.encode(newUser.getPassword())).avatar(newUser.getAvatar())
				.roles(Stream.of(UserRole.USER).collect(Collectors.toSet())).build();

		// Guardar y retornar el nuevo usuario
		return save(userEntity);
	}
}
