package com.gomcc.merchant_checker_service.service.utility.cache;


import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantModeOfPayment;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.gomcc.merchant_checker_service.service.MerchantServiceTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class MerchantWarmRedisCacheServiceTest {

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

    @Autowired
    MerchantRedisHashRepository merchantRedisHashRepository;

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
    }


    @Test
    @DisplayName("Preloaded cache should share same number of cache keys as rows in merchant service db")
    void PreloadedCacheShouldHaveSameNumberOfKeysAsDb(){
        int cacheKeyCount = merchantRedisHashRepository.findAll().size();
        int dbRows = merchantRepository.findAll().size();
        Assertions.assertEquals(cacheKeyCount, dbRows);
    }
}
