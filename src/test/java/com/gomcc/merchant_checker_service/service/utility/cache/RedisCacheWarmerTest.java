package com.gomcc.merchant_checker_service.service.utility.cache;


import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
public class RedisCacheWarmerTest {

    @InjectMocks
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
    @DisplayName("Preloaded cache should share same number of cache keys as rows in Db")
    void PreloadedCacheShouldHaveSameNumberOfKeysAsDb(){

    }
}
