package com.camora.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    int status,
    String erro,
    String mensagem,
    LocalDateTime timestamp,
    Map<String, String> detalhes
) {
    public ErrorResponse(int status, String erro, String mensagem) {
        this(status, erro, mensagem, LocalDateTime.now(), null);
    }
}
