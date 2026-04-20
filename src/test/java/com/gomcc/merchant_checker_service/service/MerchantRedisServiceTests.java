package com.gomcc.merchant_checker_service.service;


import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.google.j2objc.annotations.AutoreleasePool;
import com.redis.om.spring.RedisJSONKeyValueAdapter;
import com.redis.om.spring.client.RedisModulesClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Optional;

/*
    MerchantRedisService:
        - Connects to Redis repository
        - HGET MerchantHash
        - Use EntityStream to fuzzySearch
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MerchantRedisServiceTests {

    @MockitoBean
    MerchantRepository merchantRepository;

    @Autowired
    MerchantRedisHashRepository merchantRedisHashRepository;

    @Autowired
    RedisTemplate<String, MerchantRedisHash> redisTemplate;


    @Container
    static final GenericContainer<?> redisContainer = new GenericContainer<>(
            "redis/redis-stack-server:latest")
            .withExposedPorts(6379)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));
    @Autowired
    private MerchantRedisService merchantRedisService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private RedisModulesClient redisModulesClient;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379)); // 6379 -> 6379
    }

    @Test
    void dockerIsAvailable() {
        assertThat(DockerClientFactory.instance().isDockerAvailable()).isTrue();
    }


    @Test
    public void fuzzySearchCacheHitShouldNotCallMerchantRepository(){

        Optional<Merchant> TEST_MERCHANT = MerchantServiceTests.generateTestMerchant();
        TEST_MERCHANT.ifPresentOrElse(
                m ->
                {
                    System.out.printf("Name: %s , mcc: %s, mode: %s\n", m.getName(), m.getMcc(), m.getMode());

                    merchantRedisHashRepository.save(MerchantRedisHash.fromMerchant(m));

                    merchantRedisService.fuzzySearch(m.getName());

                    verify(merchantRepository, never()).fuzzyQueryMerchantByName(m.getName());
                },
                () -> System.out.println("TEST_MERCHANT NOT FOUND"));

    }
}
