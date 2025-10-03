package com.reetz.MeuDinheiro.service;

import com.reetz.MeuDinheiro.config.JwtUtil;
import com.reetz.MeuDinheiro.dto.CadastroDTO;
import com.reetz.MeuDinheiro.dto.LoginRequestDTO;
import com.reetz.MeuDinheiro.dto.LoginResponseDTO;
import com.reetz.MeuDinheiro.model.Usuario;
import com.reetz.MeuDinheiro.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            UsuarioService usuarioService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    public String cadastro(CadastroDTO request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));

        usuarioRepository.save(usuario);
        return "Usuário criado com sucesso";
    }

    public LoginResponseDTO login(LoginRequestDTO authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email(), authRequest.password()
                )
        );

        UserDetails userDetails = usuarioService.loadUserByEmail(authRequest.email());
        String token = jwtUtil.gerarToken(userDetails.getUsername()); // continua colocando username no token

        Usuario usuario = usuarioRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new LoginResponseDTO(token, usuario.getId());
    }
}