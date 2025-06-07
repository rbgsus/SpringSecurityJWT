package com.sorteoapp.sorteoapp.security.jwtmodel;

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
	public JwtUserResponse(String username, String avatar, String token) {
		super(username, avatar);
		this.token = token;
	}

}
