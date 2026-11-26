package com.camora.dto;

public record LocalDTO(
    Long id,
    String cep,
    String tipoLogradouro,
    String logradouro,
    String bairro,
    String cidade,
    String uf,
    String numero,
    String complemento
) {}
