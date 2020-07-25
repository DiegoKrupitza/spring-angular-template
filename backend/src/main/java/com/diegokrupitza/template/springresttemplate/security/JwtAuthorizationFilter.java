package com.diegokrupitza.template.springresttemplate.security;

import com.diegokrupitza.template.springresttemplate.config.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityProperties securityProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties) {
        super(authenticationManager);
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            UsernamePasswordAuthenticationToken authToken = getAuthToken(request);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (IllegalArgumentException | JwtException e) {
            log.debug("Invalid authorization attempt: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid authorization header or token");
            return;
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest request)
            throws JwtException, IllegalArgumentException {
        String token = request.getHeader(securityProperties.getAuthHeader());
        if (token == null || token.isEmpty() || !token.startsWith(securityProperties.getAuthTokenPrefix())) {
            throw new IllegalArgumentException("Authorization header is malformed or missing");
        }

        byte[] signingKey = securityProperties.getJwtSecret().getBytes();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build().parseClaimsJws(token.replace(securityProperties.getAuthTokenPrefix(), ""))
                .getBody();

        Long id = Long.valueOf(claims.getSubject());

        List<SimpleGrantedAuthority> authorities = ((List<?>) claims
                .get("rol")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(id, null, authorities);
    }
}
