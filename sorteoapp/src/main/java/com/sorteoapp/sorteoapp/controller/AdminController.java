package com.sorteoapp.sorteoapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.AdminEditUserDto;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.AdminEntityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {
	
	
	private final AdminEntityService adminEntityService;
	
	

	// Endpoint para editar un usuario como "admin"
	@PutMapping("/admin/edit/{id}")
	public ResponseEntity<UserEntity> editUserAdmin(@PathVariable Long id,
			@Valid @RequestBody AdminEditUserDto adminEditUserDto) {
		UserEntity actualizado = adminEntityService.updateUserAsAdmin(id, adminEditUserDto);
		return ResponseEntity.ok(actualizado);
	}	
	
	
}
