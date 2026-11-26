package com.camora.dto;

import java.util.List;

public record AtualizarPreferenciasRequest(
    List<Long> tematicasIds
) {}
