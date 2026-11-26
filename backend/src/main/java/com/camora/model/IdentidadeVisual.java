package com.camora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "identidade_visual")
public class IdentidadeVisual {

    public static final String COR_PRINCIPAL_PADRAO = "#471CA8";
    public static final String COR_SECUNDARIA_PADRAO = "#884AB2";
    public static final String COR_DESTAQUE_PADRAO = "#FF930A";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @Column(name = "cor_principal", length = 16)
    private String corPrincipal = COR_PRINCIPAL_PADRAO;

    @Column(name = "cor_secundaria", length = 16)
    private String corSecundaria = COR_SECUNDARIA_PADRAO;

    @Column(name = "cor_destaque", length = 16)
    private String corDestaque = COR_DESTAQUE_PADRAO;

    @Column(name = "usar_identidade_padrao", nullable = false)
    private boolean usarIdentidadePadrao = true;

    public static IdentidadeVisual padrao() {
        IdentidadeVisual iv = new IdentidadeVisual();
        iv.setUsarIdentidadePadrao(true);
        iv.setCorPrincipal(COR_PRINCIPAL_PADRAO);
        iv.setCorSecundaria(COR_SECUNDARIA_PADRAO);
        iv.setCorDestaque(COR_DESTAQUE_PADRAO);
        return iv;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getCorPrincipal() { return corPrincipal; }
    public void setCorPrincipal(String corPrincipal) { this.corPrincipal = corPrincipal; }

    public String getCorSecundaria() { return corSecundaria; }
    public void setCorSecundaria(String corSecundaria) { this.corSecundaria = corSecundaria; }

    public String getCorDestaque() { return corDestaque; }
    public void setCorDestaque(String corDestaque) { this.corDestaque = corDestaque; }

    public boolean isUsarIdentidadePadrao() { return usarIdentidadePadrao; }
    public void setUsarIdentidadePadrao(boolean usarIdentidadePadrao) { this.usarIdentidadePadrao = usarIdentidadePadrao; }
}
