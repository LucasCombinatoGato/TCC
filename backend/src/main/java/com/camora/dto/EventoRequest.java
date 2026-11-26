package com.camora.dto;

import com.camora.model.StatusEvento;
import com.camora.model.TipoAmbito;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventoRequest(
    @NotBlank(message = "Nome do evento/ação é obrigatório") String nome,
    String descricao,
    @NotNull(message = "Data de início é obrigatória") LocalDateTime dataInicio,
    @NotNull(message = "Data de término é obrigatória") LocalDateTime dataFim,
    String classificacaoIndicativa,
    boolean estacionamento,
    BigDecimal valorEstacionamento,
    String imagemCapaUrl,
    TipoAmbito tipoAmbito,
    StatusEvento status,
    @NotNull(message = "Temática é obrigatória") Long tematicaId,
    LocalDTO local,
    List<InformacoesDiaDTO> informacoesDias,
    @Valid List<TipoIngressoDTO> tiposIngresso
) {}
