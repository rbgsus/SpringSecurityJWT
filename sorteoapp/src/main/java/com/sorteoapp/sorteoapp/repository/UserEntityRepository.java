package com.sorteoapp.sorteoapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sorteoapp.sorteoapp.model.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByDni(String dni);

	Optional<UserEntity> findByUsername(String username);

}
