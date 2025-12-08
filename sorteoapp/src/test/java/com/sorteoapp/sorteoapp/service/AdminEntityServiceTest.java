package com.sorteoapp.sorteoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sorteoapp.sorteoapp.dto.AdminEditUserDto;
import com.sorteoapp.sorteoapp.dto.EditPerfilUserDto;
import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;
import com.sorteoapp.sorteoapp.repository.AdminEntityRepository;

class AdminEntityServiceTest {

	@Mock
	private AdminEntityRepository adminRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AdminEntityService adminService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// ----------------- findUserByUsername -----------------
	@Test
	void testFindUserByUsername() {
		UserEntity user = new UserEntity();
		when(adminRepo.findByUsername("admin")).thenReturn(Optional.of(user));
		Optional<UserEntity> res = adminService.findUserByUsername("admin");
		assertTrue(res.isPresent());
		assertEquals(user, res.get());
	}

	// ----------------- existsByEmailIgnoreCase -----------------
	@Test
	void testExistsByEmailIgnoreCase() {
		when(adminRepo.existsByEmailIgnoreCase("admin@example.com")).thenReturn(true);
		assertTrue(adminService.existsByEmailIgnoreCase("admin@example.com"));
	}

	// ----------------- findByIdOrThrow -----------------
	@Test
	void testFindByIdOrThrowFound() {
		UserEntity user = new UserEntity();
		when(adminRepo.findById(1L)).thenReturn(Optional.of(user));
		assertEquals(user, adminService.findByIdOrThrow(1L));
	}

	@Test
	void testFindByIdOrThrowNotFound() {
		when(adminRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> adminService.findByIdOrThrow(1L));
	}

	// ----------------- updateUserAsUser -----------------
	@Test
	void testUpdateUserAsUserOk() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("user1");
		dto.setEmail("user1@example.com");

		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(adminRepo.findByUsername("user1")).thenReturn(Optional.of(existing));
		when(adminRepo.existsByEmailIgnoreCase("user1@example.com")).thenReturn(false);
		when(adminRepo.save(existing)).thenReturn(existing);

		UserEntity updated = adminService.updateUserAsUser(1L, dto);
		assertEquals(existing, updated);
	}

	@Test
	void testUpdateUserAsUserUsernameTaken() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);
		UserEntity other = new UserEntity();
		other.setId(2L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("other");
		dto.setEmail("other@example.com");

		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(adminRepo.findByUsername("other")).thenReturn(Optional.of(other));

		assertThrows(UsernameAlreadyExistsException.class, () -> adminService.updateUserAsUser(1L, dto));
	}

	@Test
	void testUpdateUserAsUserEmailTaken() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);
		UserEntity other = new UserEntity();
		other.setId(2L);

		EditPerfilUserDto dto = new EditPerfilUserDto();
		dto.setUsername("user1");
		dto.setEmail("other@example.com");

		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(adminRepo.findByUsername("user1")).thenReturn(Optional.of(existing));
		when(adminRepo.existsByEmailIgnoreCase("other@example.com")).thenReturn(true);
		when(adminRepo.findByEmailIgnoreCase("other@example.com")).thenReturn(Optional.of(other));

		assertThrows(EmailAlreadyExistsException.class, () -> adminService.updateUserAsUser(1L, dto));
	}

	// ----------------- updateUserAsAdmin -----------------
	@Test
	void testUpdateUserAsAdminWithPassword() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);

		AdminEditUserDto dto = new AdminEditUserDto();
		dto.setUsername("admin1");
		dto.setEmail("admin1@example.com");
		dto.setPassword("newpass");
		dto.setRoles(Set.of(UserRole.ADMIN));

		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(adminRepo.findByUsername("admin1")).thenReturn(Optional.of(existing));
		when(adminRepo.existsByEmailIgnoreCase("admin1@example.com")).thenReturn(false);
		when(passwordEncoder.encode("newpass")).thenReturn("encodedpass");
		when(adminRepo.save(existing)).thenReturn(existing);

		UserEntity updated = adminService.updateUserAsAdmin(1L, dto);
		assertEquals(existing, updated);
		assertEquals("encodedpass", updated.getPassword());
		assertEquals(Set.of(UserRole.ADMIN), updated.getRoles());
	}

	@Test
	void testUpdateUserAsAdminWithoutPassword() {
		UserEntity existing = new UserEntity();
		existing.setId(1L);

		AdminEditUserDto dto = new AdminEditUserDto();
		dto.setUsername("admin1");
		dto.setEmail("admin1@example.com");
		dto.setPassword(null); // no cambia la password

		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(adminRepo.findByUsername("admin1")).thenReturn(Optional.of(existing));
		when(adminRepo.existsByEmailIgnoreCase("admin1@example.com")).thenReturn(false);
		when(adminRepo.save(existing)).thenReturn(existing);

		UserEntity updated = adminService.updateUserAsAdmin(1L, dto);
		assertEquals(existing, updated);
	}

	// ----------------- deleteUserById -----------------
	@Test
	void testDeleteUserByIdOk() {
		UserEntity existing = new UserEntity();
		when(adminRepo.findById(1L)).thenReturn(Optional.of(existing));

		adminService.deleteUserById(1L);
		verify(adminRepo).delete(existing);
	}

	@Test
	void testDeleteUserByIdNotFound() {
		when(adminRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> adminService.deleteUserById(1L));
	}
}
