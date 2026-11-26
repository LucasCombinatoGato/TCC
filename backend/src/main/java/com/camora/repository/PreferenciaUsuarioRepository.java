package com.camora.repository;

import com.camora.model.PreferenciaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, Long> {
    List<PreferenciaUsuario> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
