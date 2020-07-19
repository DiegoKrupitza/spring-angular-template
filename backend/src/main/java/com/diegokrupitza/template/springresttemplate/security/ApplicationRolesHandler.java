package com.diegokrupitza.template.springresttemplate.security;

import com.diegokrupitza.template.springresttemplate.domain.Role;
import com.diegokrupitza.template.springresttemplate.persistence.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Configuration
@Slf4j
public class ApplicationRolesHandler {

    private final RoleRepository roleRepository;

    public ApplicationRolesHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void saveRolesToDataBase() {
        Stream.of(ApplicationRoles.values())
                .map(ApplicationRoles::getName)
                .map(Role::new)
                .map(roleRepository::saveIfNameNotExists)
                .forEach(x -> log.info("Created new role: {}", x.getName()));
    }

}
