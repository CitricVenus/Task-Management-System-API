package com.example.taskmanager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final long jwtExpirationInMs;
    private final String issuer;


    public JwtUtil(   @Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long jwtExpirationInMs,
                      @Value("${jwt.issuer}") String issuer)
    {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.issuer = issuer;
    }


    //Create the new token
    public String generateToken(String username, String role){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role",role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Get username from token
    public String getUsernameFromToken(String token){
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token){
        Claims claims = parseClaims(token);
        return claims.get("role",String.class);
    }

    //validate token

    public boolean validateToken(String token){
        try{
            parseClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException ex) {
            // Puedes agregar logs aquí para diferentes errores
            return false;
        }
    }

    private Claims parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
