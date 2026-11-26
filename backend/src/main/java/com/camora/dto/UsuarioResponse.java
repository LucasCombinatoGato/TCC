package com.camora.dto;

import java.util.List;

public record UsuarioResponse(
    Long id,
    String cpf,
    String nome,
    String email,
    LocalDTO local,
    List<TematicaDTO> preferencias
) {}
