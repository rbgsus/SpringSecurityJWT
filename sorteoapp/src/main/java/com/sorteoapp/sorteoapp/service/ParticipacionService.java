package com.sorteoapp.sorteoapp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sorteoapp.sorteoapp.model.Participacion;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.repository.ParticipacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;

    @Transactional
    public Participacion comprarNumero(UserEntity usuario, Tarjeta tarjeta, Integer numero) {
        // Verificar que el número no esté ya comprado en la tarjeta
        if (participacionRepository.existsByTarjetaIdTarjetaAndNumero(tarjeta.getIdTarjeta(), numero)) {
            throw new IllegalArgumentException("Número ya comprado en esta tarjeta");
        }

        Participacion participacion = Participacion.builder()
                .usuario(usuario)
                .tarjeta(tarjeta)
                .numero(numero)
                .fechaCompra(LocalDateTime.now())
                .build();

        return participacionRepository.save(participacion);
    }

    public List<Participacion> getParticipacionesByUsuarioYTarjeta(Long usuarioId, Long tarjetaId) {
        return participacionRepository.findByUsuarioIdAndTarjetaIdTarjeta(usuarioId, tarjetaId);
    }
}
