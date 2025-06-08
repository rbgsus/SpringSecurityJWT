package com.sorteoapp.sorteoapp.security.jwtmodel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.GetuserDto;
import com.sorteoapp.sorteoapp.dto.UserDtoConverter;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.security.JwtUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final JwtUtils jwtutils;

	private final UserDtoConverter converter;

	@PostMapping("/auth/login")
	public ResponseEntity<JwtUserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserEntity user = (UserEntity) authentication.getPrincipal();

		String jwtToken = jwtutils.generateToken(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(convertUserEntityAndTokenToJwtUserResponse(user, jwtToken));

	}

	private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(UserEntity user, String jwtToken) {
		return JwtUserResponse.jwtUserResponseBuilder().username(user.getUsername()).avatar(user.getAvatar())
				
				.token(jwtToken).build();
	}

	public GetuserDto me(@AuthenticationPrincipal UserEntity user) {
		return converter.converterEntityToGetUserDto(user);

	}
}
