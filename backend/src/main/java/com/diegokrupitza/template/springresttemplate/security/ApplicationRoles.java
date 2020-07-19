package com.diegokrupitza.template.springresttemplate.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */
@AllArgsConstructor
@Getter
public enum ApplicationRoles {
    ADMIN(Identifier.ADMIN),
    USER(Identifier.USER);

    private String name;

    public class Identifier {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }

}

