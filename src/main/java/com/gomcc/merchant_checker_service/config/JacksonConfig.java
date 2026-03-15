package com.gomcc.merchant_checker_service.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean("startupObjectMapper")
    @Primary
    public ObjectMapper mapper(){
        System.out.println("initializing objectMapper...");

        ObjectMapper mapper = new ObjectMapper();

//        mapper.

//        mapper.activateDefaultTyping(
//                mapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//
//        System.out.println(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));

        return mapper;

    }
}
