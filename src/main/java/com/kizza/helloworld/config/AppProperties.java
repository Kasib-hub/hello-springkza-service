package com.kizza.helloworld.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "com.kizza")
@Data
public class AppProperties {
    private String cloudEnvironment = "thisIsDefault"; // this will be default, gets overwritten by .properties
    private String environment;
    private boolean skipFilter = true;
}
