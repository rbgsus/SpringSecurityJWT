package com.sorteoapp.sorteoapp.repository;

import com.sorteoapp.sorteoapp.model.Participacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {

    List<Participacion> findByUsuarioId(Long usuarioId);

    List<Participacion> findByTarjetaIdTarjeta(Long tarjetaId); 

    List<Participacion> findByUsuarioIdAndTarjetaIdTarjeta(Long usuarioId, Long idTarjeta);

    boolean existsByTarjetaIdTarjetaAndNumero(Long tarjetaId, Integer numero);
}
