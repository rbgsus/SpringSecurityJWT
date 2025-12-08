package com.sorteoapp.sorteoapp.security.jwtmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class JwtUserResponseTest {

    @Test
    void testBuilderAndGettersSetters() {
        JwtUserResponse jwt = JwtUserResponse.jwtUserResponseBuilder()
                .username("juan")
                .avatar("avatar.png")
                .token("token123")
                .build();

        assertEquals("juan", jwt.getUsername());
        assertEquals("avatar.png", jwt.getAvatar());
        assertEquals("token123", jwt.getToken());

        jwt.setUsername("pedro");
        jwt.setAvatar("avatar2.png");
        jwt.setToken("token456");

        assertEquals("pedro", jwt.getUsername());
        assertEquals("avatar2.png", jwt.getAvatar());
        assertEquals("token456", jwt.getToken());
    }
}
