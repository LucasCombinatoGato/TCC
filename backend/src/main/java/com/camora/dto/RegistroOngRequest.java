package com.camora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroOngRequest(
    @NotBlank(message = "CNPJ é obrigatório") String cnpj,
    @NotBlank(message = "Nome da ONG é obrigatório") String nome,
    String descricao,
    @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "Senha é obrigatória") String senha,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
