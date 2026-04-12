package com.gomcc.merchant_checker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash$;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.gomcc.merchant_checker_service.utility.webClient.AskMilesWebClient;
import com.redis.om.spring.search.stream.EntityStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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