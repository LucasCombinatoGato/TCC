package com.camora.controller;

import com.camora.config.UserPrincipal;
import com.camora.dto.IdentidadeVisualDTO;
import com.camora.dto.OngResponse;
import com.camora.dto.OngUpdateRequest;
import com.camora.service.OngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ongs")
@Tag(name = "ONGs", description = "Consulta pública, perfil e identidade visual de ONGs")
public class OngController {

    private final OngService ongService;

    public OngController(OngService ongService) {
        this.ongService = ongService;
    }

    @GetMapping
    @Operation(summary = "Lista todas as ONGs cadastradas")
    public ResponseEntity<List<OngResponse>> listarTodas() {
        return ResponseEntity.ok(ongService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta perfil público de uma ONG")
    public ResponseEntity<OngResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ongService.buscarPorId(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ONG')")
    @Operation(summary = "Obtém o perfil da ONG autenticada")
    public ResponseEntity<OngResponse> meuPerfil(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ongService.buscarPorId(principal.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('ONG')")
    @Operation(summary = "Atualiza dados do perfil da ONG autenticada")
    public ResponseEntity<OngResponse> atualizarMeuPerfil(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody OngUpdateRequest request) {
        return ResponseEntity.ok(ongService.atualizarPerfil(principal.getId(), request));
    }

    @PutMapping("/me/identidade-visual")
    @PreAuthorize("hasRole('ONG')")
    @Operation(summary = "Personaliza a identidade visual da ONG (White-Label)")
    public ResponseEntity<IdentidadeVisualDTO> atualizarIdentidadeVisual(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody IdentidadeVisualDTO dto) {
        return ResponseEntity.ok(ongService.atualizarIdentidadeVisual(principal.getId(), dto));
    }

    @PostMapping("/me/identidade-visual/padrao")
    @PreAuthorize("hasRole('ONG')")
    @Operation(summary = "Restaura a identidade visual padrão da plataforma Camora")
    public ResponseEntity<IdentidadeVisualDTO> restaurarIdentidadePadrao(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ongService.restaurarIdentidadePadrao(principal.getId()));
    }
}
