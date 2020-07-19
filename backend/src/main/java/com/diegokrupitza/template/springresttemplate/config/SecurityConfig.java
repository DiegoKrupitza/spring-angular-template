package com.diegokrupitza.template.springresttemplate.config;

import com.diegokrupitza.template.springresttemplate.config.properties.SecurityProperties;
import com.diegokrupitza.template.springresttemplate.security.JwtAuthenticationFilter;
import com.diegokrupitza.template.springresttemplate.security.JwtAuthorizationFilter;
import com.diegokrupitza.template.springresttemplate.security.JwtTokenizer;
import com.diegokrupitza.template.springresttemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RequestMatcher whiteListedRequests;
    private final SecurityProperties securityProperties;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public SecurityConfig(UserService userService,
                          PasswordEncoder passwordEncoder,
                          SecurityProperties securityProperties, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;

        this.whiteListedRequests = new OrRequestMatcher(securityProperties.getWhiteList().stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList()));
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityProperties, jwtTokenizer, userService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), securityProperties, userService))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(whiteListedRequests);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> permitAll = Collections.unmodifiableList(Collections.singletonList("*"));
        final List<String> permitMethods = List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(), HttpMethod.HEAD.name(),
                HttpMethod.TRACE.name());
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(permitAll);
        configuration.setAllowedOrigins(permitAll);
        configuration.setAllowedMethods(permitMethods);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
