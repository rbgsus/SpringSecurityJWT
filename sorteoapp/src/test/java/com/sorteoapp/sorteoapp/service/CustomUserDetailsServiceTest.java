package com.sorteoapp.sorteoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sorteoapp.sorteoapp.model.UserEntity;

class CustomUserDetailsServiceTest {

	@Mock
	private UserEntityService userEntityService;

	@InjectMocks
	private CustomUserDetailsService customService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// ----------------- loadUserByUsername -----------------
	@Test
	void testLoadUserByUsernameFound() {
		UserEntity user = new UserEntity();
		user.setUsername("juan");
		when(userEntityService.findUserByUsername("juan")).thenReturn(Optional.of(user));

		UserDetails details = customService.loadUserByUsername("juan");
		assertEquals(user, details);
	}

	@Test
	void testLoadUserByUsernameNotFound() {
		when(userEntityService.findUserByUsername("juan")).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> customService.loadUserByUsername("juan"));
	}

	// ----------------- loadUserById -----------------
	@Test
	void testLoadUserByIdFound() {
		UserEntity user = new UserEntity();
		user.setId(1L);
		when(userEntityService.findById(1L)).thenReturn(Optional.of(user));

		UserDetails details = customService.loadUserById(1L);
		assertEquals(user, details);
	}

	@Test
	void testLoadUserByIdNotFound() {
		when(userEntityService.findById(1L)).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> customService.loadUserById(1L));
	}
}
