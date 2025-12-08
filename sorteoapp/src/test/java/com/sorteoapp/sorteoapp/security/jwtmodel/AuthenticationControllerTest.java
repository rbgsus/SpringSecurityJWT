package com.sorteoapp.sorteoapp.security.jwtmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.sorteoapp.sorteoapp.dto.GetuserDto;
import com.sorteoapp.sorteoapp.dto.UserDtoConverter;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.security.JwtUtils;

class AuthenticationControllerTest {

	@Mock
	private AuthenticationManager authManager;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserDtoConverter converter;

	@InjectMocks
	private AuthenticationController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// ---------------- login ----------------
	@Test
	void testLoginSuccess() {
	    // Crear LoginRequest usando constructor
	    LoginRequest login = new LoginRequest("usuario1", "123");

	    UserEntity user = new UserEntity();
	    user.setUsername("usuario1");
	    user.setAvatar("avatar.png");

	    Authentication auth = mock(Authentication.class);
	    when(auth.getPrincipal()).thenReturn(user);
	    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
	    when(jwtUtils.generateToken(user)).thenReturn("token123");

	    ResponseEntity<JwtUserResponse> res = controller.login(login);


	    assertEquals("usuario1", res.getBody().getUsername());
	    assertEquals("avatar.png", res.getBody().getAvatar());
	    assertEquals("token123", res.getBody().getToken());
	}



	// ---------------- me ----------------
	@Test
	void testMe() {
		UserEntity user = new UserEntity();
		GetuserDto dto = new GetuserDto();
		when(converter.converterEntityToGetUserDto(user)).thenReturn(dto);

		GetuserDto result = controller.me(user);
		assertEquals(dto, result);
	}
}
