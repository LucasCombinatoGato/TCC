package com.camora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroProdutorRequest(
    @NotBlank(message = "CNPJ é obrigatório") String cnpj,
    @NotBlank(message = "Nome da empresa é obrigatório") String nomeEmpresa,
    @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "Senha é obrigatória") String senha,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
