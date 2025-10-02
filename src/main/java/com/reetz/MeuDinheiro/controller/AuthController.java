package com.reetz.MeuDinheiro.controller;

import com.reetz.MeuDinheiro.config.JwtUtil;
import com.reetz.MeuDinheiro.dto.CadastroDTO;
import com.reetz.MeuDinheiro.dto.CadastroResponseDTO;
import com.reetz.MeuDinheiro.dto.LoginRequestDTO;
import com.reetz.MeuDinheiro.dto.LoginResponseDTO;
import com.reetz.MeuDinheiro.model.Usuario;
import com.reetz.MeuDinheiro.repository.UsuarioRepository;
import com.reetz.MeuDinheiro.service.AuthService;
import com.reetz.MeuDinheiro.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody Usuario usuario) {
        try {
            String response = authService.signup(usuario);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO authRequest) {
        try {
            LoginResponseDTO response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

}
/*
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(400).body("Nome de usuário já existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário criado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Tentando autenticar: " + authRequest.username());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(), authRequest.password()
                    )
            );

            System.out.println("Autenticado com sucesso!");

            UserDetails userDetails = usuarioService.loadUserByUsername(authRequest.username());
            String token = jwtUtil.gerarToken(userDetails.getUsername());

            // Pega o usuário do banco
            Usuario usuario = usuarioRepository.findByUsername(authRequest.username());

            return ResponseEntity.ok(new AuthResponse(token, usuario.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@RequestBody CadastroDTO cadastroDTO) {
        try {
            Usuario novoUsuario = authService.cadastrar(cadastroDTO);

            CadastroResponseDTO response = new CadastroResponseDTO(
                    novoUsuario.getId(),
                    novoUsuario.getNome(),
                    novoUsuario.getEmail()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());

            LoginResponseDTO response = new LoginResponseDTO(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

} */
