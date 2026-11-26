package com.camora.dto;

import java.time.LocalDateTime;

public record NotificacaoResponse(
    Long id,
    Long eventoId,
    String eventoNome,
    String mensagem,
    LocalDateTime dataCriacao,
    boolean lida
) {}
