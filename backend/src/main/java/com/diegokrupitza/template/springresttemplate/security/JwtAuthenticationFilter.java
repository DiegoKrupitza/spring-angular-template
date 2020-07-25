package com.diegokrupitza.template.springresttemplate.security;

import com.diegokrupitza.template.springresttemplate.config.properties.SecurityProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties,
                                   JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        JwtRequest user;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), JwtRequest.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        }

        //Compares the user with UserServiceImpl#loadUserByUsername and check if the credentials are correct
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        log.debug("Invalid authentication attempt: {}", failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = ((User) authResult.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        // generating the jwt token based on role and id of the user
        JwtResponse jwtResponse = new JwtResponse(jwtTokenizer.getAuthToken(user.getUsername(), roles));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(toJson(jwtResponse));
        out.flush();
        log.info("Successfully authenticated user {}", user.getUsername());
    }

    /**
     * Converts the jwt token to a json string
     *
     * @param response the response to convert to json
     * @return the jwt token as json string
     * @throws JsonProcessingException in case a error happens while processing
     */
    private String toJson(JwtResponse response) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(response);
    }


}
