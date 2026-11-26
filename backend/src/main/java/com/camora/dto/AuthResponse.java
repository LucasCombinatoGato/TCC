package com.camora.dto;

import com.camora.model.Role;

public record AuthResponse(
    String token,
    Long id,
    String nome,
    String email,
    Role role,
    IdentidadeVisualDTO identidadeVisual
) {}
