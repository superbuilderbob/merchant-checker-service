package com.gomcc.merchant_checker_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean()
    @Primary
    public ObjectMapper mapper(){
        System.out.println("initializing objectMapper...");

        return new ObjectMapper();
    }
}
