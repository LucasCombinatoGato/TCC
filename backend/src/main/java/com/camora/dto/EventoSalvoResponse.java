package com.camora.dto;

import java.time.LocalDateTime;

public record EventoSalvoResponse(
    Long id,
    LocalDateTime dataSalvamento,
    EventoResponse evento
) {}
