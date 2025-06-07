package com.sorteoapp.sorteoapp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sorteoapp.sorteoapp.dto.AdminEditUserDto;
import com.sorteoapp.sorteoapp.dto.EditPerfilUserDto;
import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.repository.AdminEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEntityService extends BaseService<UserEntity, Long, AdminEntityRepository> {

	private final PasswordEncoder passwordEncoder;

	public Optional<UserEntity> findUserByUsername(String username) {
		return this.repositorio.findByUsername(username);
	}

	public boolean existsByEmailIgnoreCase(String email) {
		return this.repositorio.existsByEmailIgnoreCase(email);
	}

	public UserEntity findByIdOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	
	
	// TODO: AQUI EMPIEZA LA TAREA DE PODER EDITAR TODO
	public UserEntity updateUserAsUser(Long id, EditPerfilUserDto dto) {
		UserEntity user = findByIdOrThrow(id);

		validateUsernameAndEmailUniqueness(dto.getUsername(), dto.getEmail(), id);

		user.setDni(dto.getDni());
		user.setName(dto.getName());
		user.setUsername(dto.getUsername());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPhone(dto.getPhone());
		user.setFechaNacimiento(dto.getFechaNacimiento());
		user.setAvatar(dto.getAvatar());

		return save(user);
	}

	public UserEntity updateUserAsAdmin(Long id, AdminEditUserDto dto) {
		UserEntity user = findByIdOrThrow(id);

		validateUsernameAndEmailUniqueness(dto.getUsername(), dto.getEmail(), user.getId());

		user.setDni(dto.getDni());
		user.setName(dto.getName());
		user.setUsername(dto.getUsername());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPhone(dto.getPhone());
		user.setFechaNacimiento(dto.getFechaNacimiento());
		user.setRoles(dto.getRoles());
		user.setAvatar(dto.getAvatar());
		user.setPassword(dto.getPassword());
		
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		return save(user);
	}

	// -------------------- MÉTODOS PRIVADOS --------------------
	
	private void validateUsernameAndEmailUniqueness(String username, String email, Long currentUserId) {
		findUserByUsername(username).ifPresent(user -> {
			if (!user.getId().equals(currentUserId)) {
				throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
			}
		});

		if (existsByEmailIgnoreCase(email)) {
			UserEntity existingUser = repositorio.findByEmailIgnoreCase(email).orElse(null);
			if (existingUser != null && !existingUser.getId().equals(currentUserId)) {
				throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
			}
		}
	}

	public void deleteUserById(Long id) {
		UserEntity user = findByIdOrThrow(id); // Lanza excepción si no existe
		delete(user);
	}
}
