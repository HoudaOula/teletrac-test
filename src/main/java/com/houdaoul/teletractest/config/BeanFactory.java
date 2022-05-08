package com.houdaoul.teletractest.config;

import com.houdaoul.teletractest.util.Tokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactory {
  
  @Bean
  public Tokenizer tokenizer() {
    return new Tokenizer();
  }
}
