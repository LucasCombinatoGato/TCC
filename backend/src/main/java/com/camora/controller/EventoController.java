package com.camora.controller;

import com.camora.config.UserPrincipal;
import com.camora.dto.EventoRequest;
import com.camora.dto.EventoResponse;
import com.camora.model.TipoAmbito;
import com.camora.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos", description = "Descoberta pública, filtragem e gerenciamento de eventos e ações sociais")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @Operation(summary = "Lista eventos e ações sociais publicados com filtros e priorização por preferências")
    public ResponseEntity<List<EventoResponse>> listarPublicos(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long tematicaId,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Boolean gratuito,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) TipoAmbito tipoAmbito,
            @RequestParam(required = false) Long ongId,
            @RequestParam(required = false) Long produtorId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.listarPublicos(
                q, tematicaId, cidade, uf, gratuito, dataInicio, dataFim, tipoAmbito, ongId, produtorId, principal));
    }

    @GetMapping("/meus")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Lista os eventos/ações do produtor ou ONG autenticado")
    public ResponseEntity<List<EventoResponse>> listarMeusEventos(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.listarMeusEventos(principal));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém detalhes completos de um evento ou ação social")
    public ResponseEntity<EventoResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.buscarPorId(id, principal));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Cria um novo evento ou ação social")
    public ResponseEntity<EventoResponse> criar(
            @Valid @RequestBody EventoRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criar(request, principal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Atualiza um evento ou ação social próprio")
    public ResponseEntity<EventoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.atualizar(id, request, principal));
    }

    @PatchMapping("/{id}/publicar")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Publica um evento ou ação social")
    public ResponseEntity<EventoResponse> publicar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.publicar(id, principal));
    }

    @PatchMapping("/{id}/despublicar")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Retorna um evento publicado para rascunho")
    public ResponseEntity<EventoResponse> despublicar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.despublicar(id, principal));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Cancela um evento ou ação social e notifica seguidores")
    public ResponseEntity<EventoResponse> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(eventoService.cancelar(id, principal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ONG')")
    @Operation(summary = "Exclui permanentemente um evento próprio")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        eventoService.excluir(id, principal);
        return ResponseEntity.noContent().build();
    }
}
