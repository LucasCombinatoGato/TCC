package com.camora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dados_contato")
public class DadosContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String email;

    @Column(length = 40)
    private String telefone;

    @Column(name = "redes_sociais", columnDefinition = "TEXT")
    private String redesSociais;

    @Column(length = 255)
    private String site;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getRedesSociais() { return redesSociais; }
    public void setRedesSociais(String redesSociais) { this.redesSociais = redesSociais; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }
}
