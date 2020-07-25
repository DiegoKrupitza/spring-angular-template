package com.diegokrupitza.template.springresttemplate.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Diego Krupitza
 * @version 1.0
 * @date 25.07.20
 */
@Component
@ConfigurationProperties(prefix = "application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProperties {

    private String property1;
    private boolean templateMode;
    private List<String> propertyList;
}
