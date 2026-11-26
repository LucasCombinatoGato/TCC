package com.camora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tematica")
public class Tematica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "cor_hex", length = 16)
    private String corHex;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCorHex() { return corHex; }
    public void setCorHex(String corHex) { this.corHex = corHex; }
}
