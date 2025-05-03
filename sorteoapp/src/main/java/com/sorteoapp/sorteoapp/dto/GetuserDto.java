package com.sorteoapp.sorteoapp.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetuserDto {

	private String username;
	private String avatar;
	private String email;
	private Set<String> roles;

}
