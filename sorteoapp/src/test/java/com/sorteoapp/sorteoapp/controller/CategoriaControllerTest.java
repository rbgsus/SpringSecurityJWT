package com.sorteoapp.sorteoapp.controller;

import com.sorteoapp.sorteoapp.model.Categoria;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaControllerTest {

    @Test
    void testGetCategorias() {
        CategoriaController controller = new CategoriaController();
        List<String> categorias = controller.getCategorias();

        // Verifica que no sea null
        assertNotNull(categorias);

        // Verifica que tenga todas las categorías
        assertEquals(Categoria.values().length, categorias.size());

        // Verifica que contenga algunas categorías conocidas
        assertTrue(categorias.contains("TECNOLOGÍA"));
        assertTrue(categorias.contains("HOGAR"));
        assertTrue(categorias.contains("DEPORTE"));
    }
}
