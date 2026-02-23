package com.kizza.helloworld.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
@AllArgsConstructor
public class AppConfig {

    private final AppProperties appProperties;

    @Bean
    public String helloThere() {
        return String.format("%s and %s", appProperties.getEnvironment(), appProperties.getCloudEnvironment());
    }
}
