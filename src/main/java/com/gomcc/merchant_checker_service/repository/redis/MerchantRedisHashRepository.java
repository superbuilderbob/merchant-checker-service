package com.gomcc.merchant_checker_service.repository.redis;

import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.redis.om.spring.annotations.Query;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.List;

public interface MerchantRedisHashRepository
        extends RedisDocumentRepository<MerchantRedisHash, Long> {

    @Query("@name:%$name%")
    List<MerchantRedisHash> fuzzySearchByName(String name);
}
