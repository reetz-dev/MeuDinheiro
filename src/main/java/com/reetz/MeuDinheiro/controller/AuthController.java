package com.reetz.MeuDinheiro.controller;

import com.reetz.MeuDinheiro.dto.UsuarioDTO;
import com.reetz.MeuDinheiro.model.Usuario;
import com.reetz.MeuDinheiro.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario novoUsuario = authService.cadastrar(usuarioDTO);
            return ResponseEntity.ok(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciais) {
        try {
            String email = credenciais.get("email");
            String senha = credenciais.get("senha");

            String token = authService.login(email, senha);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
