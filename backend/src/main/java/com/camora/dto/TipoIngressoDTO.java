package com.camora.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record TipoIngressoDTO(
    Long id,
    @NotBlank(message = "Nome do ingresso é obrigatório") String nome,
    String descricao,
    BigDecimal valor,
    Integer quantidadeDisponivel,
    String beneficios,
    String linkCompra
) {}
