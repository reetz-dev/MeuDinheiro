package com.reetz.MeuDinheiro.service;

import com.reetz.MeuDinheiro.config.JwtUtil;
import com.reetz.MeuDinheiro.dto.CadastroDTO;
import com.reetz.MeuDinheiro.dto.CadastroResponseDTO;
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

    public String signup(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuário criado com sucesso";
    }

    public LoginResponseDTO login(LoginRequestDTO authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email(), authRequest.password()
                )
        );

        UserDetails userDetails = usuarioService.loadUserByUsername(authRequest.email());
        String token = jwtUtil.gerarToken(userDetails.getUsername());

        Usuario usuario = usuarioRepository.findByEmail(authRequest.email());

        return new LoginResponseDTO(token, usuario.getId());
    }
}

/*
@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Usuario cadastrar(CadastroDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));

        return usuarioRepository.save(u);
    }

    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("USER")
                .build();

        return jwtUtil.generateToken(userDetails);
    }

//    public String login(String email, String senha) {
//        Usuario user = usuarioRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        if (!passwordEncoder.matches(senha, user.getPassword())) {
//            throw new RuntimeException("Senha inválida");
//        }
//
//        return Jwts.builder()
//                .setSubject(user.getId().toString())
//                .claim("name", user.getNome())
//                .claim("role", "USER")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
//                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
//                .compact();
//    }

    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }
}

*/