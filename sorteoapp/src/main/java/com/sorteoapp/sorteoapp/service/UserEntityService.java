package com.sorteoapp.sorteoapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.dto.EditPerfilUserDto;
import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;
import com.sorteoapp.sorteoapp.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEntityService {

	private final UserEntityRepository userEntityRepository;

	private final PasswordEncoder passwordEncoder;

	public Optional<UserEntity> findUserByUsername(String username) {
		return userEntityRepository.findByUsername(username);
	}

	public boolean existsByEmailIgnoreCase(String email) {
		return userEntityRepository.existsByEmailIgnoreCase(email);
	}

	public UserEntity findByIdOrThrow(Long id) {
		return userEntityRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	public UserEntity registerUser(CreateUserDto newUser) {
		validateUser(newUser);

		UserEntity userEntity = UserEntity.builder().name(newUser.getName()).username(newUser.getUsername())
				.firstName(newUser.getFirstName()).lastName(newUser.getLastName())
				.password(passwordEncoder.encode(newUser.getPassword())).email(newUser.getEmail())
				.avatar(newUser.getAvatar()).roles(Set.of(UserRole.USER)).build();

		log.info("Registrando nuevo usuario: {}", userEntity.getUsername());
		return userEntityRepository.save(userEntity);
	}

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

		return userEntityRepository.save(user);
	}

	// -------------------- MÉTODOS PRIVADOS --------------------

	private void validateUser(CreateUserDto newUser) {
		if (newUser.getUsername() == null || findUserByUsername(newUser.getUsername()).isPresent()) {
			throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
		}

		if (!newUser.getPassword().equals(newUser.getPassword2())) {
			throw new NewUserWithDifferentPasswordsException("Las contraseñas no coinciden");
		}

		if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("El email no puede estar vacío");
		}

		if (existsByEmailIgnoreCase(newUser.getEmail())) {
			throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
		}
	}

	private void validateUsernameAndEmailUniqueness(String username, String email, Long currentUserId) {
		findUserByUsername(username).ifPresent(user -> {
			if (!user.getId().equals(currentUserId)) {
				throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
			}
		});

		if (existsByEmailIgnoreCase(email)) {
			UserEntity existingUser = userEntityRepository.findByEmailIgnoreCase(email).orElse(null);
			if (existingUser != null && !existingUser.getId().equals(currentUserId)) {
				throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
			}
		}
	}

	public void deleteUserById(Long id) {
		UserEntity user = findByIdOrThrow(id); // Lanza excepción si no existe
		userEntityRepository.delete(user);
	}

	public Optional<UserEntity> findById(Long id) {
		return userEntityRepository.findById(id);
	}

	public List<UserEntity> findAll() {
		return userEntityRepository.findAll();
	}

}
