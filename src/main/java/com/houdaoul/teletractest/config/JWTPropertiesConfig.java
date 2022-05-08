package com.houdaoul.teletractest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/jwt-config.properties")
public class JWTPropertiesConfig {
}
