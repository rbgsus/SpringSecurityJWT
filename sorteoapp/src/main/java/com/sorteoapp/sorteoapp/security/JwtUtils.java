package com.sorteoapp.sorteoapp.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Se puede cambiar por una clave fija

	private final long jwtExpirationMs = 86400000; // 1 día

	public String extractUsername(String token) {
		// Extrae el claim personalizado "username"
		return extractClaim(token, claims -> claims.get("username", String.class));
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean isTokenValid(String token, UserEntity user) {
		final Long id = extractUserId(token);
		return (id.equals(user.getId()) && !isTokenExpired(token));
	}

	public String generateToken(UserDetails userDetails) {
		if (!(userDetails instanceof UserEntity user)) {
			throw new IllegalArgumentException("UserDetails no es instancia de UserEntity");
		}

		return Jwts.builder().setSubject(String.valueOf(user.getId())) // ahora el subject es el ID
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.claim("username", user.getUsername())
				.claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.joining(", ")))
				.claim("fullname", user.getName() + " " + user.getLastName()).signWith(secretKey).compact();
	}

	public Long extractUserId(String token) {
		// Extrae el subject, que ahora es el ID del usuario
		return Long.parseLong(extractClaim(token, Claims::getSubject));
	}

}
