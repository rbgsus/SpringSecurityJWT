package com.sorteoapp.sorteoapp.security.jwtmodel;

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
	public JwtUserResponse(String username, String avatar, String email, Set<String> roles, String token) {
		super(username, avatar, email, roles);
		this.token = token;
	}

}
