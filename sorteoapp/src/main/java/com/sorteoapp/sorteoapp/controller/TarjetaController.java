package com.sorteoapp.sorteoapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.GetTarjetaDto;
import com.sorteoapp.sorteoapp.dto.TarjetaDtoConverter;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.TarjetaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cards") // La ruta "/tarjeta" ahora está en plural
@RequiredArgsConstructor
@Validated
public class TarjetaController {

	private final TarjetaService tarjetaService;
	private final TarjetaDtoConverter tarjetaDtoConverter;

	/**
	 * Endpoint para crear una nueva tarjeta. Asocia automáticamente al usuario
	 * autenticado.
	 * 
	 * @param tarjeta Tarjeta enviada en el cuerpo de la solicitud.
	 * @return la tarjeta creada en forma de DTO.
	 */
	@PostMapping("/")
	public ResponseEntity<GetTarjetaDto> nuevaTarjeta(@Valid @RequestBody Tarjeta tarjeta) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity usuario = (UserEntity) auth.getPrincipal();

		tarjeta.setUsuario(usuario); // Asociar el usuario autenticado
		tarjetaService.nuevaTarjeta(tarjeta);
		GetTarjetaDto res = tarjetaDtoConverter.converterTarjetaToGetTarjetaDto(tarjeta);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	/**
	 * Endpoint provisional para obtener todas las tarjetas sin paginación. Se
	 * recomienda eliminarlo cuando se implemente completamente la paginación.
	 * 
	 * @return Lista de tarjetas en formato DTO.
	 */
	@GetMapping("/all")
	public ResponseEntity<List<GetTarjetaDto>> obtenerTodasTarjetas2() {
		List<Tarjeta> todasTarjetas = tarjetaService.findAll();

		List<GetTarjetaDto> todasTarjetasDto = todasTarjetas.stream()
				.map(tarjetaDtoConverter::converterTarjetaToGetTarjetaDto).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(todasTarjetasDto);
	}

	/**
	 * Endpoint para obtener todas las tarjetas con paginación.
	 * 
	 * @param page Página solicitada (por defecto 0)
	 * @param size Cantidad de elementos por página (por defecto 12)
	 * @return Página de tarjetas en formato DTO.
	 */
	@GetMapping("/") // TODO: poner /all
	public ResponseEntity<Page<GetTarjetaDto>> obtenerTodasTarjetas(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Tarjeta> tarjetas = tarjetaService.findAll(pageable);

		Page<GetTarjetaDto> todasTarjetasDto = tarjetas.map(tarjetaDtoConverter::converterTarjetaToGetTarjetaDto);

		return ResponseEntity.status(HttpStatus.OK).body(todasTarjetasDto);
	}
}
