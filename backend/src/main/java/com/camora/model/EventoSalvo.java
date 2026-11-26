package com.camora.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "evento_salvo",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "evento_id"})
)
public class EventoSalvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "data_salvamento", nullable = false)
    private LocalDateTime dataSalvamento = LocalDateTime.now();

    public EventoSalvo() {}

    public EventoSalvo(Usuario usuario, Evento evento) {
        this.usuario = usuario;
        this.evento = evento;
        this.dataSalvamento = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public LocalDateTime getDataSalvamento() { return dataSalvamento; }
    public void setDataSalvamento(LocalDateTime dataSalvamento) { this.dataSalvamento = dataSalvamento; }
}
