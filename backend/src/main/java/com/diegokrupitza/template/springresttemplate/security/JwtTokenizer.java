package com.diegokrupitza.template.springresttemplate.security;

import com.diegokrupitza.template.springresttemplate.config.properties.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Component
public class JwtTokenizer {

    private final SecurityProperties securityProperties;
    private final byte[] signingKeyBytes;

    public JwtTokenizer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        signingKeyBytes = securityProperties.getJwtSecret().getBytes();
    }

    /**
     * Generates a new Jwt based on a given subject (in this case <code>userId</code>) and the roles of the user
     *
     * @param userId the id of the user which works as subject
     * @param roles  the roles for the given token
     * @return the newly generated Jwt
     */
    public String getAuthToken(String userId, List<String> roles) {
        return Jwts.builder()
                .setHeaderParam("typ", securityProperties.getJwtType())
                .setIssuer(securityProperties.getJwtIssuer())
                .setAudience(securityProperties.getJwtAudience())
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
                .claim("rol", roles)
                .signWith(Keys.hmacShaKeyFor(signingKeyBytes), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Check if a given token is valid or not
     *
     * @param token the token to check if valid or not
     * @throws BadCredentialsException in case token is expired or invalid
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKeyBytes))
                    .build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Expired or invalid JWT token", e);
        }
    }

    /**
     * Gets the claims of a given token
     *
     * @param token the token to get the claims of
     * @return the claims of the token
     */
    public Optional<Jws<Claims>> getClaims(Optional<String> token) {
        if (token.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKeyBytes)).build().parseClaimsJws(token.get()));
    }

    /**
     * Gets the subject of a given token
     *
     * @param token the token to get the subject of
     * @return the subject of the token
     */
    public String getTokenSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKeyBytes)).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Handles the refresh process of a token
     *
     * @param bearerStr the old still valid token of the user
     * @return the newly created token
     * @throws BadCredentialsException in case the given <code>bearerStr</code> is not valid or expired
     */
    public String refreshToken(String bearerStr) {
        if (bearerStr == null || bearerStr.isEmpty() || !bearerStr.startsWith(securityProperties.getAuthTokenPrefix())) {
            throw new IllegalArgumentException("Authorization header is malformed or missing");
        }

        String token = bearerStr.replaceAll("Bearer ", "");
        validateToken(token);

        Optional<Jws<Claims>> claimsOpt = getClaims(Optional.of(token));
        if (claimsOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid token claims");
        }

        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                .build().parseClaimsJws(token.replace(securityProperties.getAuthTokenPrefix(), ""))
                .getBody();

        String tokenSubject = getTokenSubject(token);
        List<String> roles = (List<String>) claims.get("rol");

        return getAuthToken(tokenSubject, roles);
    }

}
