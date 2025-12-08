package com.sorteoapp.sorteoapp.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sorteoapp.sorteoapp.model.UserEntity;

class UserPerfilDtoConverterTest {

    private UserPerfilDtoConverter converter;

    @BeforeEach
    void setup() {
        converter = new UserPerfilDtoConverter();
    }

    @Test
    void testConverterEntityTotUserPerfilDto() {
        UserEntity user = UserEntity.builder()
                .dni("12345678X")
                .name("Nombre")
                .username("user1")
                .firstName("First")
                .email("email@test.com")
                .phone("123456789")
                .build();

        var dto = converter.converterEntityTotUserPerfilDto(user);

        assertEquals("12345678X", dto.getDni());
        assertEquals("Nombre", dto.getName());
        assertEquals("user1", dto.getUsername());
        assertEquals("First", dto.getFirstName());
        assertEquals("email@test.com", dto.getEmail());
        assertEquals("123456789", dto.getPhone());
    }
}
