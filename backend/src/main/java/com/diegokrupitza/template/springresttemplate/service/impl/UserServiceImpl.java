package com.diegokrupitza.template.springresttemplate.service.impl;

import com.diegokrupitza.template.springresttemplate.domain.Role;
import com.diegokrupitza.template.springresttemplate.domain.User;
import com.diegokrupitza.template.springresttemplate.exceptions.NotFoundException;
import com.diegokrupitza.template.springresttemplate.persistence.UserRepository;
import com.diegokrupitza.template.springresttemplate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with the username %s does not exist!", username))
                );
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for authentication spring security");
        try {
            User user = findByUsername(username);

            String[] roles = (String[]) user.getRoles().stream().map(Role::getName).toArray(String[]::new);
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(roles);

            return new org.springframework.security.core.userdetails.User(
                    Long.toString(user.getId()),
                    user.getPassword(),
                    grantedAuthorities);

        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

    }
}
