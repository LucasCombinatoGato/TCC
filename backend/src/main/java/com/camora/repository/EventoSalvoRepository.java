package com.camora.repository;

import com.camora.model.EventoSalvo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EventoSalvoRepository extends JpaRepository<EventoSalvo, Long> {
    List<EventoSalvo> findByUsuarioIdOrderByDataSalvamentoDesc(Long usuarioId);
    List<EventoSalvo> findByEventoId(Long eventoId);
    Optional<EventoSalvo> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);
    boolean existsByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);
    void deleteByEventoId(Long eventoId);
}
