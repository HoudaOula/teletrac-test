package com.houdaoul.teletractest.util;

import com.houdaoul.teletractest.domain.Authority;
import com.houdaoul.teletractest.domain.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Tokenizer {
  
  private static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);
  
  private static final String USERNAME_KEY = "username";

  private static final String ROLES_KEY = "roles";

  @Value("${jwt.secret-key}")
  private String SECRET_KEY;
  
  public Optional<User> parseToken(String token) {
    try {
      Claims body = Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token)
          .getBody();
      
      User user = new User();
      user.setUsername(body.getSubject());
      user.setAuthorities((List<Authority>) body.get(ROLES_KEY));
      
      return Optional.of(user);
      
    } catch (JwtException | ClassCastException e) {
      return Optional.empty();
    }
  }
  
  public String generateToken(User user) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put(USERNAME_KEY, user.getUsername());
    claims.put(ROLES_KEY, user.getAuthorities());
    
    return Jwts.builder().setClaims(claims)
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }
  
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty", e.getMessage());
    }
    return false;
  }
  
}
