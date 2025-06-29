package com.sorteoapp.sorteoapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sorteoapp.sorteoapp.dto.CompraParticipacionRequest;
import com.sorteoapp.sorteoapp.dto.ParticipacionDto;
import com.sorteoapp.sorteoapp.dto.ParticipacionDtoConverter;
import com.sorteoapp.sorteoapp.model.Participacion;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.ParticipacionService;
import com.sorteoapp.sorteoapp.service.TarjetaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/participaciones")
@RequiredArgsConstructor
public class ParticipacionController {

    private final ParticipacionService participacionService;
    private final ParticipacionDtoConverter participacionDtoConverter;
    private final TarjetaService tarjetaService;

    @PostMapping("/comprar")
    public ResponseEntity<?> comprarNumero(
            Authentication auth,
            @RequestBody CompraParticipacionRequest request) {

        UserEntity usuario = (UserEntity) auth.getPrincipal();

        Tarjeta tarjeta = tarjetaService.findById(request.getTarjetaId())
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        if (request.getNumero() < 1 || request.getNumero() > tarjeta.getPrecio()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El número debe estar entre 1 y " + tarjeta.getPrecio());
        }

        Participacion participacion = participacionService.comprarNumero(usuario, tarjeta, request.getNumero());

        return ResponseEntity.status(HttpStatus.CREATED).body(participacionDtoConverter.convertirEntityToDTO(participacion));
    }



    @GetMapping("/usuario/{usuarioId}/tarjeta/{tarjetaId}")
    public ResponseEntity<List<ParticipacionDto>> obtenerParticipaciones(
            @PathVariable Long usuarioId,
            @PathVariable Long tarjetaId) {

        List<Participacion> participaciones = participacionService.getParticipacionesByUsuarioYTarjeta(usuarioId, tarjetaId);

        List<ParticipacionDto> dtos = participaciones.stream()
                .map(participacionDtoConverter::convertirEntityToDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}