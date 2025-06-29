package com.sorteoapp.sorteoapp.dto;

import org.springframework.stereotype.Component;

import com.sorteoapp.sorteoapp.model.Participacion;

@Component
public class ParticipacionDtoConverter {

    public ParticipacionDto convertirEntityToDTO(Participacion p) {
        ParticipacionDto dto = new ParticipacionDto();
        dto.setId(p.getId());
        dto.setUsuarioId(p.getUsuario().getId());
        dto.setUsuarioName(p.getUsuario().getName());
        dto.setTarjetaId(p.getTarjeta().getIdTarjeta());
        dto.setTarjetaNombre(p.getTarjeta().getNombreTarjeta());
        dto.setTarjetaDueño(p.getTarjeta().getUsuario().getUsername());
        dto.setNumero(p.getNumero());
        dto.setFechaCompra(p.getFechaCompra());
        return dto;
    }
}
