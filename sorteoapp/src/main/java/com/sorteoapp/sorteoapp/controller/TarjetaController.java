package com.sorteoapp.sorteoapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.GetTarjetaDto;
import com.sorteoapp.sorteoapp.dto.TarjetaDtoConverter;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.service.TarjetaService;

@RestController
@RequestMapping("/tarjetas") // La ruta "/tarjeta" ahora está en plural
public class TarjetaController {

	@Autowired
	private TarjetaService tarjetaService;

	@Autowired
	private TarjetaDtoConverter tarjetaDtoConverter;

	@PostMapping("/")
	public ResponseEntity<Tarjeta> nuevaTarjeta(@RequestBody Tarjeta tarjeta) {

		if (tarjeta.getNombreTarjeta().isEmpty() || tarjeta.getPrecio() == null || tarjeta.getCategoria() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		// Guardar la nueva tarjeta
		return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaService.save(tarjeta));
	}

	// TODO: Eliminar este endpoint
	@GetMapping("/todas")
	public ResponseEntity<List<GetTarjetaDto>> obtenerTodasTarjetas2() {
		List<Tarjeta> todasTarjetas = tarjetaService.findAll();

		List<GetTarjetaDto> todasTarjetasDto = todasTarjetas.stream()
				.map(tarjetaDtoConverter::converterTarjetaToGetTarjetaDto).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(todasTarjetasDto);
	}
	@GetMapping("/")
	public ResponseEntity<Page<GetTarjetaDto>> obtenerTodasTarjetas(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<Tarjeta> tarjetas = tarjetaService.findAll(pageable);

		Page<GetTarjetaDto> todasTarjetasDto = tarjetas.map(tarjetaDtoConverter::converterTarjetaToGetTarjetaDto);

		return ResponseEntity.status(HttpStatus.OK).body(todasTarjetasDto);

	}

}
