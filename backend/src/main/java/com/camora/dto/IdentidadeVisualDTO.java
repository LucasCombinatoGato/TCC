package com.camora.dto;

public record IdentidadeVisualDTO(
    Long id,
    String logo,
    String corPrincipal,
    String corSecundaria,
    String corDestaque,
    boolean usarIdentidadePadrao
) {}
