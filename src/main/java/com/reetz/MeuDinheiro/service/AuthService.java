package com.reetz.MeuDinheiro.service;

import com.reetz.MeuDinheiro.model.Usuario;
import com.reetz.MeuDinheiro.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Injeta a chave secreta do application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    public Usuario cadastrar(Usuario u) {
        if (usuarioRepository.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        u.setSenha(passwordEncoder.encode(u.getSenha())); // hash seguro
        return usuarioRepository.save(u);
    }

    public String login(String email, String senha) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        // Gera JWT usando a chave injetada
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name", user.getNome())
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1h
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
