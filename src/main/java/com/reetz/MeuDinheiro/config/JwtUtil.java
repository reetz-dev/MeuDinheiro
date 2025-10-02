package com.reetz.MeuDinheiro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

   @Value("${jwt.secret}")
    private String secret;

    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String gerarToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 1 hora
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public String extrairUsername(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token); // se for inválido, vai lançar exceção
        return decodedJWT.getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }


//        // Extrai username do token com tratamento de exceções
//        public String extractUsername (String token){
//            try {
//                return extractClaim(token, Claims::getSubject);
//            } catch (Exception e) {
//                System.out.println("Erro ao extrair username do token: " + e.getMessage());
//                return null;
//            }
//        }
//
//        public <T > T extractClaim(String token, Function < Claims, T > claimsResolver) {
//            final Claims claims = extractAllClaims(token);
//            return claimsResolver.apply(claims);
//        }
//
//        private Claims extractAllClaims (String token){
//            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//        }
//
//        public boolean validateToken (String token, UserDetails userDetails){
//            String username = extractUsername(token);
//            return username != null && username.equals(userDetails.getUsername());
//        }
//
//        public String generateToken (UserDetails userDetails){
//            Map<String, Object> claims = new HashMap<>();
//            return createToken(claims, userDetails.getUsername());
//        }
//
//        private String createToken (Map < String, Object > claims, String subject){
//            long expirationTime = 1000 * 60 * 60 * 10; // 10 horas
//            return Jwts.builder()
//                    .setClaims(claims)
//                    .setSubject(subject)
//                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                    .signWith(SignatureAlgorithm.HS256, secret)
//                    .compact();
//        }
    }
}


