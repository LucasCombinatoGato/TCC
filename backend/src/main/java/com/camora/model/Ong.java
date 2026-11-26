package com.camora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ong")
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false, length = 180)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "local_id")
    private Local local;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dados_contato_id")
    private DadosContato dadosContato;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identidade_visual_id")
    private IdentidadeVisual identidadeVisual = IdentidadeVisual.padrao();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }

    public DadosContato getDadosContato() { return dadosContato; }
    public void setDadosContato(DadosContato dadosContato) { this.dadosContato = dadosContato; }

    public IdentidadeVisual getIdentidadeVisual() { return identidadeVisual; }
    public void setIdentidadeVisual(IdentidadeVisual identidadeVisual) { this.identidadeVisual = identidadeVisual; }
}
