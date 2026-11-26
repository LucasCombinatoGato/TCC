package com.camora.dto;

public record OngResponse(
    Long id,
    String cnpj,
    String nome,
    String descricao,
    String email,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
