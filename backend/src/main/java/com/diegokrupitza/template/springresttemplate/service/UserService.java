package com.diegokrupitza.template.springresttemplate.service;

import com.diegokrupitza.template.springresttemplate.domain.User;
import com.diegokrupitza.template.springresttemplate.exceptions.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
public interface UserService extends UserDetailsService {

    /**
     * Gets all User stored into the system
     *
     * @return all users stored into the system
     */
    List<User> getAllUsers();

    /**
     * Find a user by its username
     *
     * @param username the username to search for
     * @return the user with the given username
     * @throws NotFoundException when the username does not exist
     */
    User findByUsername(String username);
}
