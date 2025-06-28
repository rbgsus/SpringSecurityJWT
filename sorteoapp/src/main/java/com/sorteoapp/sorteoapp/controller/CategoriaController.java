package com.sorteoapp.sorteoapp.controller;

import com.sorteoapp.sorteoapp.model.Categoria;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@GetMapping
	public List<String> getCategorias() {
		return Arrays.stream(Categoria.values()).map(Enum::name).toList();
	}
}
