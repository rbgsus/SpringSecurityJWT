package com.sorteoapp.sorteoapp.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.model.UserRole;

class CreateUserDtoConverterTest {

    private PasswordEncoder passwordEncoder;
    private CreateUserDtoConverter converter;

    @BeforeEach
    void setup() {
        passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        converter = new CreateUserDtoConverter(passwordEncoder);
    }

    @Test
    void testCreateUserToUserEntity() {
        var newUser = new CreateUserDto();
        newUser.setUsername("user1");
        newUser.setPassword("1234");
        newUser.setName("Nombre");
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setEmail("email@test.com");
        newUser.setAvatar("avatar.png");

        UserEntity entity = converter.createUserToUserEntity(newUser);

        assertEquals("user1", entity.getUsername());
        assertEquals("encodedPassword", entity.getPassword());
        assertEquals(Set.of(UserRole.USER), entity.getRoles());
    }
}
