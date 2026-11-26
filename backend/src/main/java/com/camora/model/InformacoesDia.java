package com.camora.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "informacoes_dia")
public class InformacoesDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_intervalo")
    private LocalTime horaIntervalo;

    @Column(name = "hora_final", nullable = false)
    private LocalTime horaFinal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraIntervalo() { return horaIntervalo; }
    public void setHoraIntervalo(LocalTime horaIntervalo) { this.horaIntervalo = horaIntervalo; }

    public LocalTime getHoraFinal() { return horaFinal; }
    public void setHoraFinal(LocalTime horaFinal) { this.horaFinal = horaFinal; }
}
