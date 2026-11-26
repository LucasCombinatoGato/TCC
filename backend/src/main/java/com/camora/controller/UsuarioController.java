package com.camora.controller;

import com.camora.config.UserPrincipal;
import com.camora.dto.AtualizarPreferenciasRequest;
import com.camora.dto.EventoSalvoResponse;
import com.camora.dto.UsuarioResponse;
import com.camora.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Usuários", description = "Perfil, preferências de temáticas e eventos salvos do usuário comum")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    @Operation(summary = "Obtém o perfil e preferências do usuário autenticado")
    public ResponseEntity<UsuarioResponse> meuPerfil(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(usuarioService.buscarPerfil(principal.getId()));
    }

    @PutMapping("/me/preferencias")
    @Operation(summary = "Atualiza as temáticas preferidas do usuário")
    public ResponseEntity<UsuarioResponse> atualizarPreferencias(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AtualizarPreferenciasRequest request) {
        return ResponseEntity.ok(usuarioService.atualizarPreferencias(principal.getId(), request));
    }

    @GetMapping("/me/eventos-salvos")
    @Operation(summary = "Lista os eventos salvos pelo usuário")
    public ResponseEntity<List<EventoSalvoResponse>> listarEventosSalvos(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(usuarioService.listarEventosSalvos(principal.getId()));
    }

    @PostMapping("/me/eventos-salvos/{eventoId}")
    @Operation(summary = "Salva um evento para acompanhamento")
    public ResponseEntity<EventoSalvoResponse> salvarEvento(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long eventoId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.salvarEvento(principal.getId(), eventoId));
    }

    @DeleteMapping("/me/eventos-salvos/{eventoId}")
    @Operation(summary = "Remove um evento da lista de salvos")
    public ResponseEntity<Void> removerEventoSalvo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long eventoId) {
        usuarioService.removerEventoSalvo(principal.getId(), eventoId);
        return ResponseEntity.noContent().build();
    }
}
