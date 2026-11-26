package com.camora.controller;

import com.camora.config.UserPrincipal;
import com.camora.dto.NotificacaoResponse;
import com.camora.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Notificações", description = "Avisos de alterações em eventos salvos")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    @Operation(summary = "Lista todas as notificações do usuário autenticado")
    public ResponseEntity<List<NotificacaoResponse>> listar(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(principal.getId()));
    }

    @GetMapping("/nao-lidas/count")
    @Operation(summary = "Retorna a quantidade de notificações não lidas")
    public ResponseEntity<Map<String, Long>> contarNaoLidas(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(Map.of("naoLidas", notificacaoService.contarNaoLidas(principal.getId())));
    }

    @PatchMapping("/{id}/lida")
    @Operation(summary = "Marca uma notificação específica como lida")
    public ResponseEntity<NotificacaoResponse> marcarComoLida(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id, principal.getId()));
    }

    @PatchMapping("/ler-todas")
    @Operation(summary = "Marca todas as notificações do usuário como lidas")
    public ResponseEntity<Void> marcarTodasComoLidas(@AuthenticationPrincipal UserPrincipal principal) {
        notificacaoService.marcarTodasComoLidas(principal.getId());
        return ResponseEntity.noContent().build();
    }
}
