package com.diegokrupitza.template.springresttemplate.endpoints;

import com.diegokrupitza.template.springresttemplate.security.JwtResponse;
import com.diegokrupitza.template.springresttemplate.security.JwtTokenizer;
import com.diegokrupitza.template.springresttemplate.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@RestController
@RequestMapping("/authentication")
public class AuthController {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    public AuthController(UserService userService, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @GetMapping("/refresh")
    public JwtResponse getRefreshedToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerStr) {
        return new JwtResponse(this.jwtTokenizer.refreshToken(bearerStr));
    }

}
