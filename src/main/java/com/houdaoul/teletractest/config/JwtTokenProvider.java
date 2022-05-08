package com.houdaoul.teletractest.config;

import com.houdaoul.teletractest.domain.User;
import com.houdaoul.teletractest.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private final DatasourceUserDetails datasourceUserDetails;

  @Value("${jwt.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt.token.expire-length}")
  private long VALIDITY;

  public JwtTokenProvider(DatasourceUserDetails datasourceUserDetails) {
    this.datasourceUserDetails = datasourceUserDetails;
  }

  public String createToken(User user) {
    
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put(Constants.AUTHORITIES_KEY, user.getAuthorities().stream().map(a -> a.getAuthority())
            .collect(Collectors.joining(",")));

    Date now = new Date();
    Date validity = new Date(now.getTime() + VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    String username = getUsername(token);
    UserDetails userDetails = datasourceUserDetails.loadUserByUsername(username);

    Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(Constants.AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }
  
  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader(Constants.AUTHORIZATION_HEADER);
    if (bearerToken != null && bearerToken.startsWith(Constants.AUTHORIZATION_BEARER)) {
      return bearerToken.substring(Constants.AUTHORIZATION_BEARER.length());
    }
    return null;
  }
  
  public boolean validateToken(String token) throws Exception {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      logger.info("Valid token!");
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      logger.info("Problem validating token!");
      throw new Exception("Expired or invalid JWT token");
    }
  }
}
