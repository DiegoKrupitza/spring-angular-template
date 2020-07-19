package com.diegokrupitza.template.springresttemplate.security;

import com.diegokrupitza.template.springresttemplate.config.properties.SecurityProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import java.util.Date;
import java.util.List;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Component
public class JwtTokenizer {

    private final SecurityProperties securityProperties;

    public JwtTokenizer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String getAuthToken(String id, List<String> roles) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        return Jwts.builder()
                .setHeaderParam("typ", securityProperties.getJwtType())
                .setIssuer(securityProperties.getJwtIssuer())
                .setAudience(securityProperties.getJwtAudience())
                .setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
                .claim("rol", roles)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .compact();
    }
}
