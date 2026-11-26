package com.camora.repository;

import com.camora.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    Optional<Produtor> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}
