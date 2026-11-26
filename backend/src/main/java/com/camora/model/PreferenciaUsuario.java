package com.camora.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "preferencia_usuario",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "tematica_id"})
)
public class PreferenciaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tematica_id", nullable = false)
    private Tematica tematica;

    public PreferenciaUsuario() {}

    public PreferenciaUsuario(Usuario usuario, Tematica tematica) {
        this.usuario = usuario;
        this.tematica = tematica;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Tematica getTematica() { return tematica; }
    public void setTematica(Tematica tematica) { this.tematica = tematica; }
}
