package com.camora.dto;

public record ProdutorUpdateRequest(
    String nomeEmpresa,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
