package com.sorteoapp.sorteoapp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;

class JwtUtilsTest {

	private JwtUtils jwtUtils;
	private UserEntity user;

	@BeforeEach
	void setup() {
		// Secret de prueba (mínimo 256 bits para HMAC SHA)
		String secret = "01234567890123456789012345678901";
		jwtUtils = new JwtUtils(secret);

		// Usuario de prueba
		user = UserEntity.builder().id(1L).username("testuser").name("Nombre").lastName("Apellido")
				.roles(Set.of(UserRole.USER)).build();
	}

	@Test
	void testGenerateAndValidateToken() {
		String token = jwtUtils.generateToken(user);

		assertNotNull(token);

		// Validar extracción de claims
		assertEquals("testuser", jwtUtils.extractUsername(token));
		assertEquals(user.getId(), jwtUtils.extractUserId(token));
		assertFalse(jwtUtils.extractExpiration(token).before(new Date()));

		// Validar token
		assertTrue(jwtUtils.isTokenValid(token, user));
	}

}
