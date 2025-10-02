package com.reetz.MeuDinheiro.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reetz.MeuDinheiro.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@WebFilter("/*")
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Autorização recebida: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            System.out.println("Token: " + token);

            try {
                String username = jwtUtil.extrairUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Usuário autenticado: " + username);
                }
            } catch (Exception e) {
                System.out.println("Erro ao validar token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    @Component
//    public class JwtUtil {
//
//        private final Algorithm algorithm;
//
//        public JwtUtil(@Value("${jwt.secret}") String secretKey) {
//            this.algorithm = Algorithm.HMAC256(secretKey);
//        }
//
//        public String gerarToken(String username) {
//            long expirationTime = 1000 * 60 * 60; // 1 hora
//            return JWT.create()
//                    .withSubject(username)
//                    .withIssuedAt(new Date())
//                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
//                    .sign(algorithm);
//        }
//
//        public String extrairUsername(String token) {
//            DecodedJWT decodedJWT = JWT.require(algorithm)
//                    .build()
//                    .verify(token); // se for inválido, vai lançar exceção
//            return decodedJWT.getSubject();
//        }
//
//        public boolean tokenValido(String token) {
//            try {
//                DecodedJWT jwt = JWT.require(algorithm)
//                        .build()
//                        .verify(token);
//                return jwt.getExpiresAt().after(new Date());
//            } catch (Exception e) {
//                return false;
//            }
//        }
//    }}
//
//    private final JwtUtil jwtUtil;
//    private final UsuarioService usuarioService;
//
//    public JwtFilter(JwtUtil jwtUtil, UsuarioService usuarioService) {
//        this.jwtUtil = jwtUtil;
//        this.usuarioService = usuarioService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        System.out.println("Filtro JWT chamado para: " + request.getServletPath());
//
//        String path = request.getServletPath();
//
//        // Ignora as rotas públicas
//        if (path.startsWith("/auth/")) {
//            System.out.println("ENTROU");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        String username = null;
//
//        try {
//            username = jwtUtil.extractUsername(token);
//            System.out.println(token);
//            System.out.println("EXTRAIU O TOKEN");
//        } catch (Exception e) {
//            System.out.println("Token inválido ou expirado: " + e.getMessage());
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = usuarioService.loadUserByUsername(username);
//
//            if (jwtUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
