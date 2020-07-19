package com.diegokrupitza.template.springresttemplate.endpoints;

import com.diegokrupitza.template.springresttemplate.service.UserService;
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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

}
