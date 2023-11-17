package com.admin.catalogo.infrastructure.configiration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.admin.catalogo.infrastructure.configiration.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
    
}
