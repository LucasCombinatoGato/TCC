package com.camora.service;

import com.camora.dto.TematicaDTO;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.Tematica;
import com.camora.repository.TematicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TematicaService {

    private final TematicaRepository tematicaRepository;
    private final MapperService mapper;

    public TematicaService(TematicaRepository tematicaRepository, MapperService mapper) {
        this.tematicaRepository = tematicaRepository;
        this.mapper = mapper;
    }

    public List<TematicaDTO> listarTodas() {
        return tematicaRepository.findAll().stream()
                .map(mapper::toTematicaDTO)
                .toList();
    }

    public Tematica buscarEntidadePorId(Long id) {
        return tematicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temática não encontrada com ID: " + id));
    }
}
