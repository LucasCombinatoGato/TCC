package com.camora.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tipo_ingresso")
public class TipoIngresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(name = "quantidade_disponivel")
    private Integer quantidadeDisponivel;

    @Column(columnDefinition = "TEXT")
    private String beneficios;

    @Column(name = "link_compra", columnDefinition = "TEXT")
    private String linkCompra;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) { this.quantidadeDisponivel = quantidadeDisponivel; }

    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }

    public String getLinkCompra() { return linkCompra; }
    public void setLinkCompra(String linkCompra) { this.linkCompra = linkCompra; }
}
