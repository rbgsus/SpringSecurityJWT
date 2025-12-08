package com.sorteoapp.sorteoapp.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sorteoapp.sorteoapp.model.Categoria;
import com.sorteoapp.sorteoapp.model.Imagen;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;

class TarjetaDtoConverterTest {

    private TarjetaDtoConverter converter;

    @BeforeEach
    void setup() {
        converter = new TarjetaDtoConverter();
    }

    @Test
    void testConverterTarjetaToGetTarjetaDto() {
        UserEntity user = UserEntity.builder().username("usuario1").build();
        Imagen img = new Imagen();
        img.setId(1L);
        img.setContenidoBase64("abc123");

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(10L);
        tarjeta.setUsuario(user);
        tarjeta.setNombreTarjeta("Tarjeta prueba");
        tarjeta.setDescripcion("Descripcion prueba");
        tarjeta.setPrecio(100.0);
        tarjeta.setCategoria(Categoria.DEPORTE);
        tarjeta.setImagenes(List.of(img));

        var dto = converter.converterTarjetaToGetTarjetaDto(tarjeta);

        assertEquals(tarjeta.getIdTarjeta(), dto.getIdTarjeta());
        assertEquals(user.getUsername(), dto.getUserName());
        assertEquals("Tarjeta prueba", dto.getNombreTarjeta());
        assertEquals("Descripcion prueba", dto.getDescripcion());
        assertEquals(100.0, dto.getPrecio());
        assertEquals(Categoria.DEPORTE, dto.getCategoria());
        assertEquals(1, dto.getImagenes().size());
        assertEquals("abc123", dto.getImagenes().get(0).getContenidoBase64());
    }
    
    
    @Test
    void testConverterCrearTarjetaDtoToTarjeta() {
        // Crear DTO de prueba
        CrearTarjetaDto dto = new CrearTarjetaDto();
        dto.setNombreTarjeta("Mi tarjeta");
        dto.setDescripcion("Descripcion prueba");
        dto.setPrecio(50.0);
        dto.setCategoria(Categoria.DEPORTE);
        dto.setImagenesBase64(List.of("img1Base64", "img2Base64"));

        // Crear usuario
        UserEntity usuario = UserEntity.builder().username("usuario1").build();

        // Convertir
        Tarjeta tarjeta = converter.converterCrearTarjetaDtoToTarjeta(dto, usuario);

        // Verificar campos
        assertEquals("Mi tarjeta", tarjeta.getNombreTarjeta());
        assertEquals("Descripcion prueba", tarjeta.getDescripcion());
        assertEquals(50.0, tarjeta.getPrecio());
        assertEquals(Categoria.DEPORTE, tarjeta.getCategoria());
        assertEquals(usuario, tarjeta.getUsuario());

        // Verificar imágenes
        assertEquals(2, tarjeta.getImagenes().size());
        assertEquals("img1Base64", tarjeta.getImagenes().get(0).getContenidoBase64());
        assertEquals("img2Base64", tarjeta.getImagenes().get(1).getContenidoBase64());

        // Verificar relación bidireccional
        for (Imagen img : tarjeta.getImagenes()) {
            assertEquals(tarjeta, img.getTarjeta());
        }
    }
}
