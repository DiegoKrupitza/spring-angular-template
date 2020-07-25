package com.diegokrupitza.template.springresttemplate.endpoints;

import com.diegokrupitza.template.springresttemplate.config.properties.ApplicationProperties;
import com.diegokrupitza.template.springresttemplate.domain.User;
import com.diegokrupitza.template.springresttemplate.security.ApplicationRoles;
import com.diegokrupitza.template.springresttemplate.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@RestController
public class UserController {

    private final UserService userService;
    private final ApplicationProperties applicationProperties;

    public UserController(UserService userService, ApplicationProperties applicationProperties) {
        this.userService = userService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/")
    public String helloWorld() {
        return applicationProperties.isTemplateMode() ? "Properties are still in template mode" : "Hello World";
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/admin")
    @Secured(ApplicationRoles.Identifier.ADMIN)
    public String adminEndpoint() {
        return "Admin endpoint!";
    }

}
