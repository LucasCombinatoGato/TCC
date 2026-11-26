package com.camora.controller;

import com.camora.config.UserPrincipal;
import com.camora.dto.IdentidadeVisualDTO;
import com.camora.dto.ProdutorResponse;
import com.camora.dto.ProdutorUpdateRequest;
import com.camora.service.ProdutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtores")
@Tag(name = "Produtores", description = "Consulta pública, perfil e identidade visual de produtores")
public class ProdutorController {

    private final ProdutorService produtorService;

    public ProdutorController(ProdutorService produtorService) {
        this.produtorService = produtorService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtores cadastrados")
    public ResponseEntity<List<ProdutorResponse>> listarTodos() {
        return ResponseEntity.ok(produtorService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta perfil público de um produtor")
    public ResponseEntity<ProdutorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtorService.buscarPorId(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PRODUCER')")
    @Operation(summary = "Obtém o perfil do produtor autenticado")
    public ResponseEntity<ProdutorResponse> meuPerfil(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(produtorService.buscarPorId(principal.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PRODUCER')")
    @Operation(summary = "Atualiza dados do perfil do produtor autenticado")
    public ResponseEntity<ProdutorResponse> atualizarMeuPerfil(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody ProdutorUpdateRequest request) {
        return ResponseEntity.ok(produtorService.atualizarPerfil(principal.getId(), request));
    }

    @PutMapping("/me/identidade-visual")
    @PreAuthorize("hasRole('PRODUCER')")
    @Operation(summary = "Personaliza a identidade visual do produtor (White-Label)")
    public ResponseEntity<IdentidadeVisualDTO> atualizarIdentidadeVisual(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody IdentidadeVisualDTO dto) {
        return ResponseEntity.ok(produtorService.atualizarIdentidadeVisual(principal.getId(), dto));
    }

    @PostMapping("/me/identidade-visual/padrao")
    @PreAuthorize("hasRole('PRODUCER')")
    @Operation(summary = "Restaura a identidade visual padrão da plataforma Camora")
    public ResponseEntity<IdentidadeVisualDTO> restaurarIdentidadePadrao(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(produtorService.restaurarIdentidadePadrao(principal.getId()));
    }
}
