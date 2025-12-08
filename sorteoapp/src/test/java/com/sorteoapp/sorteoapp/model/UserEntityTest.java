package com.sorteoapp.sorteoapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserEntityTest {

	@Test
	void testUserEntityBuilderAndFields() {
		UserEntity user = UserEntity.builder().id(1L).dni("12345678A").name("nombre").username("nombre123")
				.firstName("apellido").lastName("segundo-apellido").email("nombre@example.com").phone("600123123")
				.password("1111").fechaNacimiento(LocalDate.of(1995, 1, 1)).roles(Set.of(UserRole.USER)).build();

		assertEquals("nombre123", user.getUsername());
		assertEquals("1111", user.getPassword());
		assertEquals(1L, user.getId());
		assertEquals("apellido", user.getFirstName());
		assertTrue(user.getRoles().contains(UserRole.USER));

	}

	@Test
	void testAgregarTarjeta() {
		UserEntity user = UserEntity.builder().id(1L).dni("12345678A").name("nombre").username("nombre123")
				.firstName("apellido").lastName("segundo-apellido").email("nombre@example.com").phone("600123123")
				.password("1111").fechaNacimiento(LocalDate.of(1995, 1, 1)).roles(Set.of(UserRole.USER)).build();

		Tarjeta t1 = new Tarjeta();
		List<Tarjeta> l = List.of(t1);

		user.setTarjetas(l);

		assertEquals(1, user.getTarjetas().size());
		assertEquals(t1, l.get(0));
	    assertTrue(user.getRoles().contains(UserRole.USER));

	}
	

	@Test
	void testUserDetailsMethods() {
		UserEntity user = UserEntity.builder().username("test").password("secret").roles(Set.of(UserRole.ADMIN))
				.build();

		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isCredentialsNonExpired());
		assertTrue(user.isEnabled());
		assertNotNull(user.getAuthorities());
		
	}
	
	@Test
	void testGetAuthoritiesRolesNull() {
	    UserEntity user = new UserEntity();
	    user.setRoles(null); // roles = null

	    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

	    assertNotNull(authorities);
	    assertTrue(authorities.isEmpty());
	}

}