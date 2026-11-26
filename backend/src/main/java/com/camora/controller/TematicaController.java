package com.camora.controller;

import com.camora.dto.TematicaDTO;
import com.camora.service.MapperService;
import com.camora.service.TematicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tematicas")
@Tag(name = "Temáticas", description = "Consulta das categorias e temáticas de eventos")
public class TematicaController {

    private final TematicaService tematicaService;
    private final MapperService mapper;

    public TematicaController(TematicaService tematicaService, MapperService mapper) {
        this.tematicaService = tematicaService;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista todas as temáticas disponíveis")
    public ResponseEntity<List<TematicaDTO>> listarTodas() {
        return ResponseEntity.ok(tematicaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma temática pelo ID")
    public ResponseEntity<TematicaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toTematicaDTO(tematicaService.buscarEntidadePorId(id)));
    }
}
