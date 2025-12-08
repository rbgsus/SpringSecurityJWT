package com.sorteoapp.sorteoapp.security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.UserEntityService;
import com.sorteoapp.sorteoapp.security.JwtAuthenticationFilter;
import com.sorteoapp.sorteoapp.security.JwtUtils;

import jakarta.servlet.FilterChain;

class JwtAuthenticationFilterTest {

    private JwtUtils jwtUtils;
    private UserEntityService userService;
    private JwtAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtUtils = mock(JwtUtils.class);
        userService = mock(UserEntityService.class);
        filterChain = mock(FilterChain.class);

        filter = new JwtAuthenticationFilter(jwtUtils, userService);

        // Limpiar contexto de seguridad antes de cada test
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilter_noAuthorizationHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilter_validToken_userExists() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String token = "Bearer validtoken";
        request.addHeader("Authorization", token);

        UserEntity user = UserEntity.builder().id(1L).username("testuser").build();

        when(jwtUtils.extractUserId("validtoken")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(jwtUtils.isTokenValid("validtoken", user)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void testDoFilter_validToken_userNotFound() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String token = "Bearer validtoken";
        request.addHeader("Authorization", token);

        when(jwtUtils.extractUserId("validtoken")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
