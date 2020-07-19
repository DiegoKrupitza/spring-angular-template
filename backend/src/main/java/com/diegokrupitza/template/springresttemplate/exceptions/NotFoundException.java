package com.diegokrupitza.template.springresttemplate.exceptions;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 2020-07-19
 */

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}

