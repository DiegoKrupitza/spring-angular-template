package com.diegokrupitza.template.springresttemplate.config.properties;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This configuration class offers all necessary security properties in an immutable manner
 */
@Configuration
public class SecurityProperties {

    // In this case we need field injection since constructor inject would cause circular dependencies
    @Autowired
    private Auth auth;

    @Autowired
    private Jwt jwt;

    public String getAuthHeader() {
        return auth.getHeader();
    }

    public String getAuthTokenPrefix() {
        return auth.getPrefix();
    }

    public String getLoginUri() {
        return auth.getLoginUri();
    }

    public List<String> getWhiteList() {
        return auth.getWhiteList();
    }

    public String getJwtSecret() {
        return jwt.getSecret();
    }

    public String getJwtType() {
        return jwt.getType();
    }

    public String getJwtIssuer() {
        return jwt.getIssuer();
    }

    public String getJwtAudience() {
        return jwt.getAudience();
    }

    public Long getJwtExpirationTime() {
        return jwt.getExpirationTime();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.auth")
    protected Auth auth() {
        return new Auth();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt")
    protected Jwt jwt() {
        return new Jwt();
    }

    @NoArgsConstructor
    @Data
    protected class Auth {
        private String header;
        private String prefix;
        private String loginUri;
        private List<String> whiteList;
    }

    @NoArgsConstructor
    @Data
    protected class Jwt {
        private String secret;
        private String type;
        private String issuer;
        private String audience;
        private Long expirationTime;
    }
}

