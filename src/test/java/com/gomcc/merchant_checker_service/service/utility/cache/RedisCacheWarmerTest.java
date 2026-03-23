package com.gomcc.merchant_checker_service.service.utility.cache;


import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantModeOfPayment;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import com.gomcc.merchant_checker_service.service.MerchantServiceTests;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.Set;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RedisCacheWarmerTest {

    @Autowired
    private RedisTemplate<String, Merchant> redisTemplate;

    private static final String CACHE_NAME = "dev-merchant-name";
    private static final String TEST_KEY = "TEST";

    final Merchant TEST_MERCHANT = MerchantServiceTests.generateTestMerchant().orElse(
            Merchant.builder()
            .id(10L)
            .name("Test Merchant")
            .mcc(1234L)
            .description("Test Merchant Description")
            .mode(MerchantModeOfPayment.ONLINE_WEB)
            .build()
    );

    @Autowired
    MerchantRepository merchantRepository;

    /**
     * When:
     *  - app starts and warms up cache

     * Then:
     *  - hit `merchants/{merchantId}` endpoint
     *  - verify MerchantService does not call MerchantRepository
     *
     *  After:
     *  - flush all cache
     */

    @Test
    @DisplayName("Test cache connection is successful")
    void CacheConnectionShouldBeSuccessful(){
        /**
         * Assert id instead of other fields due to serialization/deserialization challenge where:
         * - redisTemplate.opsForValue().get("test") returns fields with null value except for id field
         */
        System.out.println("CacheConnectionShouldBeSuccessful");
        redisTemplate.opsForValue().set(TEST_KEY, TEST_MERCHANT);
        Merchant merchant = redisTemplate.opsForValue().get(TEST_KEY);
        Assertions.assertNotNull(merchant);
        Assertions.assertEquals(TEST_MERCHANT.getId(), merchant.getId());
        Assertions.assertEquals(TEST_MERCHANT.getMcc(), merchant.getMcc());
        Assertions.assertEquals(TEST_MERCHANT.getDescription(), merchant.getDescription());
        Assertions.assertEquals(TEST_MERCHANT.getName(), merchant.getName());
    }


    @Test
    @DisplayName("Preloaded cache should share same number of cache keys as rows in merchant service db")
    void PreloadedCacheShouldHaveSameNumberOfKeysAsDb(){
        System.out.println("PreloadedCacheShouldHaveSameNumberOfKeysAsDb");
        Set<String> cacheKeys = redisTemplate.keys(CACHE_NAME + "*");
        int cacheKeysCount = cacheKeys.size();
        int dbRows = merchantRepository.findAll().size();
        Assertions.assertEquals(cacheKeysCount, dbRows);
    }


    @AfterAll
    static void tearDown(@Autowired RedisTemplate<String, Merchant> redisTemplate){
        assert redisTemplate.getConnectionFactory() != null;
        redisTemplate.delete(TEST_KEY);
    }
}
