package com.camora.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record InformacoesDiaDTO(
    Long id,
    LocalDate data,
    LocalTime horaInicio,
    LocalTime horaIntervalo,
    LocalTime horaFinal
) {}
