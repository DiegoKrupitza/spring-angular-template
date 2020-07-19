package com.diegokrupitza.template.springresttemplate.persistence;

import com.diegokrupitza.template.springresttemplate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
}
