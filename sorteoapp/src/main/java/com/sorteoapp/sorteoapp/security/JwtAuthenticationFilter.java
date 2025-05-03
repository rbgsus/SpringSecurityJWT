package com.sorteoapp.sorteoapp.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.UserEntityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final UserEntityService userEntityService;

	// Constructor para inyectar las dependencias
	public JwtAuthenticationFilter(JwtUtils jwtUtils, UserEntityService userEntityService) {
		this.jwtUtils = jwtUtils;
		this.userEntityService = userEntityService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final Long userId;

		// Si el header Authorization no está presente o no empieza con "Bearer ",
		// continuamos sin hacer nada
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extraemos el JWT desde el header
		jwt = authHeader.substring(7);

		// Extraemos el ID del usuario del token
		userId = jwtUtils.extractUserId(jwt); // Asegúrate que este método devuelva Long

		// Si el ID del usuario es válido y no hay autenticación ya presente
		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Buscamos al usuario con ese ID en la base de datos
			Optional<UserEntity> optionalUser = userEntityService.findById(userId);

			// Si el usuario existe y el token es válido, autenticamos al usuario
			if (optionalUser.isPresent() && jwtUtils.isTokenValid(jwt, optionalUser.get())) {
				UserEntity user = optionalUser.get();

				// Creamos un nuevo objeto de autenticación
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());

				// Establecemos los detalles de la autenticación
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Guardamos la autenticación en el contexto de seguridad
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Continuamos con el siguiente filtro de la cadena
		filterChain.doFilter(request, response);
	}
}
