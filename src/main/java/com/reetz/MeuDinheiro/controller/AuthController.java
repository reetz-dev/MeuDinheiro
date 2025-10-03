package com.reetz.MeuDinheiro.controller;

import com.reetz.MeuDinheiro.dto.CadastroDTO;
import com.reetz.MeuDinheiro.dto.LoginRequestDTO;
import com.reetz.MeuDinheiro.dto.LoginResponseDTO;
import com.reetz.MeuDinheiro.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody CadastroDTO request) {
        try {
            String response = authService.cadastro(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO authRequest) {
        try {
            LoginResponseDTO response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}