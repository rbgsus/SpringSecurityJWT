package com.sorteoapp.sorteoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sorteoapp.sorteoapp.dto.CreateUserDto;
import com.sorteoapp.sorteoapp.dto.CreateUserDtoConverter;
import com.sorteoapp.sorteoapp.dto.EditPerfilUserDto;
import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.repository.UserEntityRepository;

public class UserEntityServiceTest {

	@Mock
	UserEntityRepository userRepo;

	@Mock
	CreateUserDtoConverter converter;

	@InjectMocks
	UserEntityService userEntityService;

	public UserEntityServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void findUserByUsername() {
		UserEntity ue = new UserEntity();
		Optional<UserEntity> opt = Optional.of(ue);
		when(userRepo.findByUsername("nick")).thenReturn(opt);
		Optional<UserEntity> res = userEntityService.findUserByUsername("nick");
		assertEquals(opt, res);

	}

	@Test
	void existsByEmailIgnoreCase() {
		when(userRepo.existsByEmailIgnoreCase("nombre@gmail")).thenReturn(true);
		assertTrue(userEntityService.existsByEmailIgnoreCase("nombre@gmail"));
	}

	@Test
	void findByIdOrThrowFound() {
		UserEntity ue = new UserEntity();

		Optional<UserEntity> opt = Optional.of(ue);
		when(userRepo.findById(1L)).thenReturn(opt);
		UserEntity res = userEntityService.findByIdOrThrow(1L);

		assertEquals(opt.get(), res);

	}

	@Test
	void testFindByIdOrThrowNotFound() {
		when(userRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userEntityService.findByIdOrThrow(1L));
	}

	@Test
	void testRegisterUserOk() {
		CreateUserDto dto = new CreateUserDto();
		dto.setUsername("juan");
		dto.setPassword("123");
		dto.setPassword2("123");
		dto.setEmail("juan@example.com");

		UserEntity userEntity = new UserEntity();
		when(converter.createUserToUserEntity(dto)).thenReturn(userEntity);
		when(userRepo.save(userEntity)).thenReturn(userEntity);
		when(userRepo.findByUsername("juan")).thenReturn(Optional.empty());
		when(userRepo.existsByEmailIgnoreCase("juan@example.com")).thenReturn(false);

		UserEntity saved = userEntityService.registerUser(dto);
		assertEquals(userEntity, saved);
	}

	// ----------------- registerUser con excepciones -----------------
	@Test
	void testRegisterUserUsernameExists() {
		CreateUserDto dto = new CreateUserDto();
		dto.setUsername("juan");
		when(userRepo.findByUsername("juan")).thenReturn(Optional.of(new UserEntity()));
		assertThrows(UsernameAlreadyExistsException.class, () -> userEntityService.registerUser(dto));
	}

	@Test
	void testRegisterUserPasswordsDontMatch() {
		CreateUserDto dto = new CreateUserDto();
		dto.setUsername("juan");
		dto.setPassword("123");
		dto.setPassword2("321");
		when(userRepo.findByUsername("juan")).thenReturn(Optional.empty());
		assertThrows(NewUserWithDifferentPasswordsException.class, () -> userEntityService.registerUser(dto));
	}

	@Test
	void testRegisterUserEmailNullOrExists() {
		CreateUserDto dto = new CreateUserDto();
		dto.setUsername("juan");
		dto.setPassword("123");
		dto.setPassword2("123");

		// email null
		dto.setEmail(null);
		when(userRepo.findByUsername("juan")).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> userEntityService.registerUser(dto));

		// email ya existe
		dto.setEmail("juan@example.com");
		when(userRepo.existsByEmailIgnoreCase("juan@example.com")).thenReturn(true);
		assertThrows(EmailAlreadyExistsException.class, () -> userEntityService.registerUser(dto));
	}

	// ----------------- updateUserAsUser -----------------
	@Test
	void testUpdateUserAsUserOk() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("juan");
		dto.setEmail("juan@example.com");

		when(userRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(userRepo.findByUsername("juan")).thenReturn(Optional.of(existing));
		when(userRepo.existsByEmailIgnoreCase("juan@example.com")).thenReturn(false);
		when(userRepo.save(existing)).thenReturn(existing);

		UserEntity updated = userEntityService.updateUserAsUser(1L, dto);
		assertEquals(existing, updated);
	}

	@Test
	void testUpdateUserAsUserUsernameTaken() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);
		UserEntity other = new UserEntity();
		other.setId(2L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("otro");
		dto.setEmail("otro@example.com");

		when(userRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(userRepo.findByUsername("otro")).thenReturn(Optional.of(other));

		assertThrows(UsernameAlreadyExistsException.class, () -> userEntityService.updateUserAsUser(1L, dto));
	}

	@Test
	void testUpdateUserAsUserEmailTaken() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);
		UserEntity other = new UserEntity();
		other.setId(2L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("juan");
		dto.setEmail("otro@example.com");

		when(userRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(userRepo.findByUsername("juan")).thenReturn(Optional.of(existing));
		when(userRepo.existsByEmailIgnoreCase("otro@example.com")).thenReturn(true);
		when(userRepo.findByEmailIgnoreCase("otro@example.com")).thenReturn(Optional.of(other));

		assertThrows(EmailAlreadyExistsException.class, () -> userEntityService.updateUserAsUser(1L, dto));
	}

	// ----------------- deleteUserById -----------------
	@Test
	void testDeleteUserByIdOk() {
		UserEntity existing = new UserEntity();
		when(userRepo.findById(1L)).thenReturn(Optional.of(existing));

		userEntityService.deleteUserById(1L);

		verify(userRepo).delete(existing);
	}

	@Test
	void testDeleteUserByIdNotFound() {
		when(userRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userEntityService.deleteUserById(1L));
	}

	// ----------------- findById y findAll -----------------
	@Test
	void testFindById() {
		UserEntity user = new UserEntity();
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		Optional<UserEntity> result = userEntityService.findById(1L);
		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	void testFindAll() {
		List<UserEntity> list = List.of(new UserEntity());
		when(userRepo.findAll()).thenReturn(list);
		List<UserEntity> result = userEntityService.findAll();
		assertEquals(1, result.size());
	}

}
