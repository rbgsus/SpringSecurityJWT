package com.sorteoapp.sorteoapp.security.jwtmodel;

import java.time.LocalDate;
import java.util.Set;

import com.sorteoapp.sorteoapp.dto.GetuserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtUserResponse extends GetuserDto {

	private String token;

	@Builder(builderMethodName = "jwtUserResponseBuilder")
	public JwtUserResponse( LocalDate fecha,String username, String avatar, String email, Set<String> roles, String token) {
		super(fecha, username, avatar, email, roles);
		this.token = token;
	}

}
