package com.camora.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "classificacao_indicativa", length = 30)
    private String classificacaoIndicativa;

    @Column(nullable = false)
    private boolean estacionamento;

    @Column(name = "valor_estacionamento", precision = 10, scale = 2)
    private BigDecimal valorEstacionamento = BigDecimal.ZERO;

    @Column(name = "imagem_capa_url", columnDefinition = "TEXT")
    private String imagemCapaUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ambito", nullable = false, length = 20)
    private TipoAmbito tipoAmbito = TipoAmbito.EVENTO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEvento status = StatusEvento.RASCUNHO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tematica_id")
    private Tematica tematica;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "local_id")
    private Local local;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produtor_id")
    private Produtor produtor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("data ASC, horaInicio ASC")
    private List<InformacoesDia> informacoesDias = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoIngresso> tiposIngresso = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public IdentidadeVisual resolverIdentidadeVisual() {
        if (produtor != null && produtor.getIdentidadeVisual() != null) {
            return produtor.getIdentidadeVisual();
        }
        if (ong != null && ong.getIdentidadeVisual() != null) {
            return ong.getIdentidadeVisual();
        }
        return IdentidadeVisual.padrao();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public String getClassificacaoIndicativa() { return classificacaoIndicativa; }
    public void setClassificacaoIndicativa(String classificacaoIndicativa) { this.classificacaoIndicativa = classificacaoIndicativa; }

    public boolean isEstacionamento() { return estacionamento; }
    public void setEstacionamento(boolean estacionamento) { this.estacionamento = estacionamento; }

    public BigDecimal getValorEstacionamento() { return valorEstacionamento; }
    public void setValorEstacionamento(BigDecimal valorEstacionamento) { this.valorEstacionamento = valorEstacionamento; }

    public String getImagemCapaUrl() { return imagemCapaUrl; }
    public void setImagemCapaUrl(String imagemCapaUrl) { this.imagemCapaUrl = imagemCapaUrl; }

    public TipoAmbito getTipoAmbito() { return tipoAmbito; }
    public void setTipoAmbito(TipoAmbito tipoAmbito) { this.tipoAmbito = tipoAmbito; }

    public StatusEvento getStatus() { return status; }
    public void setStatus(StatusEvento status) { this.status = status; }

    public Tematica getTematica() { return tematica; }
    public void setTematica(Tematica tematica) { this.tematica = tematica; }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }

    public Produtor getProdutor() { return produtor; }
    public void setProdutor(Produtor produtor) { this.produtor = produtor; }

    public Ong getOng() { return ong; }
    public void setOng(Ong ong) { this.ong = ong; }

    public List<InformacoesDia> getInformacoesDias() { return informacoesDias; }
    public void setInformacoesDias(List<InformacoesDia> informacoesDias) { this.informacoesDias = informacoesDias; }

    public List<TipoIngresso> getTiposIngresso() { return tiposIngresso; }
    public void setTiposIngresso(List<TipoIngresso> tiposIngresso) { this.tiposIngresso = tiposIngresso; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
