package com.camora.repository;

import com.camora.model.Tematica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TematicaRepository extends JpaRepository<Tematica, Long> {
    Optional<Tematica> findByNomeIgnoreCase(String nome);
}
