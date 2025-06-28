package com.sorteoapp.sorteoapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sorteoapp.sorteoapp.model.Tarjeta;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

	
	List<Tarjeta> findByUsuarioId(Long idUsuario);

	
}
