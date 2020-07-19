package com.diegokrupitza.template.springresttemplate.persistence;

import com.diegokrupitza.template.springresttemplate.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    /**
     * Saves a new role if the given role name does not exist.
     * In case the role does exists nothing is done
     *
     * @param role the role to save if not existing
     * @return the newly created role or when the role already exists the already existing one
     */
    default Role saveIfNameNotExists(Role role) {
        Optional<Role> nameExistsOptional = findByName(role.getName());
        if (nameExistsOptional.isEmpty()) {
            return save(role);
        }
        return nameExistsOptional.get();
    }

}
