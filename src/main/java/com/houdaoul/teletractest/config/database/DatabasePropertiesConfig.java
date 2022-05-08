package com.houdaoul.teletractest.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/database-config.properties")
public class DatabasePropertiesConfig {
}
