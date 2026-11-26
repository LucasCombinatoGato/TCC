package com.camora.dto;

public record OngUpdateRequest(
    String nome,
    String descricao,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
