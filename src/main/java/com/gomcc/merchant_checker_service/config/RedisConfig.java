package com.gomcc.merchant_checker_service.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.model.Merchant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    private final Duration CACHE_ABSOLUTE_TTL = Duration.ofMinutes(1);

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
                mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        System.out.println("initializing redisCacheManager");

//        System.out.println(mapper.typ);
        System.out.println("redisCacheManager:::" + mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));

//        mapper.convertValue(LinkedHashMap.class, Merchant.class);
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(CACHE_ABSOLUTE_TTL))
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues())
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer())))
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new JacksonJsonRedisSerializer<>(Merchant.class))))
                .build();
    }

    @Bean(name = "RedisTemplate")
    public RedisTemplate<String, Merchant> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String, Merchant> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JacksonJsonRedisSerializer<>(Merchant.class));

        return redisTemplate;
    }
}