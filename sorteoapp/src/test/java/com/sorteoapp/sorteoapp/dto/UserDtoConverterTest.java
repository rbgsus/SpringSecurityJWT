package com.sorteoapp.sorteoapp.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sorteoapp.sorteoapp.model.UserEntity;

class UserDtoConverterTest {

    private UserDtoConverter converter;

    @BeforeEach
    void setup() {
        converter = new UserDtoConverter();
    }

    @Test
    void testConverterEntityToGetUserDto() {
        UserEntity user = UserEntity.builder()
                .username("user1")
                .avatar("avatar.png")
                .build();

        var dto = converter.converterEntityToGetUserDto(user);

        assertEquals("user1", dto.getUsername());
        assertEquals("avatar.png", dto.getAvatar());
    }
}
