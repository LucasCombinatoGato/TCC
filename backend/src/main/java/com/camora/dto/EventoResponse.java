package com.camora.dto;

import com.camora.model.StatusEvento;
import com.camora.model.TipoAmbito;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventoResponse(
    Long id,
    String nome,
    String descricao,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String classificacaoIndicativa,
    boolean estacionamento,
    BigDecimal valorEstacionamento,
    String imagemCapaUrl,
    TipoAmbito tipoAmbito,
    StatusEvento status,
    TematicaDTO tematica,
    LocalDTO local,
    Long organizadorId,
    String organizadorNome,
    String organizadorTipo,
    DadosContatoDTO organizadorContato,
    IdentidadeVisualDTO identidadeVisual,
    List<InformacoesDiaDTO> informacoesDias,
    List<TipoIngressoDTO> tiposIngresso,
    boolean gratuito,
    boolean priorizado,
    boolean salvoPeloUsuario,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao
) {}
