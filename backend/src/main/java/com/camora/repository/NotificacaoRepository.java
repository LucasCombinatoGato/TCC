package com.camora.repository;

import com.camora.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
    long countByUsuarioIdAndLidaFalse(Long usuarioId);
    void deleteByEventoId(Long eventoId);
}
