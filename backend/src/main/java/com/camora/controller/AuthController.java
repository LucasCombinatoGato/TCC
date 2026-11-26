package com.camora.controller;

import com.camora.dto.*;
import com.camora.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Cadastro e login de Usuários, Produtores e ONGs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário, produtor ou ONG e retorna JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/usuario")
    @Operation(summary = "Cadastra um novo usuário comum")
    public ResponseEntity<AuthResponse> registrarUsuario(@Valid @RequestBody RegistroUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarUsuario(request));
    }

    @PostMapping("/register/produtor")
    @Operation(summary = "Cadastra um novo produtor de eventos")
    public ResponseEntity<AuthResponse> registrarProdutor(@Valid @RequestBody RegistroProdutorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarProdutor(request));
    }

    @PostMapping("/register/ong")
    @Operation(summary = "Cadastra uma nova ONG")
    public ResponseEntity<AuthResponse> registrarOng(@Valid @RequestBody RegistroOngRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarOng(request));
    }
}
