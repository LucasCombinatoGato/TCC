package com.camora.repository;

import com.camora.model.Evento;
import com.camora.model.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {
    List<Evento> findByProdutorIdOrderByDataCriacaoDesc(Long produtorId);
    List<Evento> findByOngIdOrderByDataCriacaoDesc(Long ongId);
    List<Evento> findByOngIdAndStatusOrderByDataInicioAsc(Long ongId, StatusEvento status);
    List<Evento> findByProdutorIdAndStatusOrderByDataInicioAsc(Long produtorId, StatusEvento status);
}
