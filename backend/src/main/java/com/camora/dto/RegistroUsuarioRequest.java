package com.camora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record RegistroUsuarioRequest(
    @NotBlank(message = "CPF é obrigatório") String cpf,
    @NotBlank(message = "Nome é obrigatório") String nome,
    @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "Senha é obrigatória") String senha,
    LocalDTO local,
    List<Long> tematicasPreferidasIds
) {}
