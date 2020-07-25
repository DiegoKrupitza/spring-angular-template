package com.diegokrupitza.template.springresttemplate.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
    private String token;
}

