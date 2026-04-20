package com.gomcc.merchant_checker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import com.gomcc.merchant_checker_service.utility.webClient.AskMilesWebClient;
import com.redis.om.spring.search.stream.EntityStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    final String CACHE_NAME = "dev-merchant-name:";

    // To Do: Move redisTemplate calls into CacheService

    private HashOperations<String, String, Object> hashOperations;

    private final MerchantRedisService merchantRedisService;

    private final AskMilesWebClient askMilesWebClient;

    private final ObjectMapper mapper;

    private final EntityStream entityStream;

    public List<MerchantResponseDto> fuzzySearch(String name){
        return merchantRedisService.fuzzySearch(name);
    }

    public List<PublicAskMilesResponse> getMiles(String searchWord){
        return askMilesWebClient.query(searchWord);
    }
}