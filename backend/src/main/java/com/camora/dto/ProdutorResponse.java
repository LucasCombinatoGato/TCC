package com.camora.dto;

public record ProdutorResponse(
    Long id,
    String cnpj,
    String nomeEmpresa,
    String email,
    LocalDTO local,
    DadosContatoDTO dadosContato,
    IdentidadeVisualDTO identidadeVisual
) {}
