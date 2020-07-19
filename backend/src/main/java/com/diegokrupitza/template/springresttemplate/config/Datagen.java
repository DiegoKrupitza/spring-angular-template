package com.diegokrupitza.template.springresttemplate.config;

import com.diegokrupitza.template.springresttemplate.domain.Role;
import com.diegokrupitza.template.springresttemplate.domain.User;
import com.diegokrupitza.template.springresttemplate.persistence.RoleRepository;
import com.diegokrupitza.template.springresttemplate.persistence.UserRepository;
import com.diegokrupitza.template.springresttemplate.security.ApplicationRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Configuration
@DependsOn("applicationRolesHandler")
@Slf4j
public class Datagen {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public Datagen(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void generateData() {
        log.info("Starting to add dummy data!");

        List<Role> allUsers = roleRepository.findAll();
        List<Role> simpleUser = Collections.singletonList(roleRepository.findByName(ApplicationRoles.Identifier.USER).get());

        List<User> users = Arrays.asList(
                new User("test", passwordEncoder.encode("12345"), simpleUser),
                new User("admin", passwordEncoder.encode("12345"), allUsers),
                new User(UUID.randomUUID().toString(), passwordEncoder.encode("12345"), allUsers),
                new User(UUID.randomUUID().toString(), passwordEncoder.encode("12345"), allUsers),
                new User(UUID.randomUUID().toString(), passwordEncoder.encode("12345"), allUsers)
        );

        userRepository.saveAll(users);
    }
}
