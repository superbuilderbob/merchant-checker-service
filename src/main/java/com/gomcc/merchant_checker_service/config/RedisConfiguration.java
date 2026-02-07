package com.gomcc.merchant_checker_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    private final Duration CACHE_ABSOLUTE_TTL = Duration.ofMinutes(1);

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(CACHE_ABSOLUTE_TTL))
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues())
                .build();

    }
}
