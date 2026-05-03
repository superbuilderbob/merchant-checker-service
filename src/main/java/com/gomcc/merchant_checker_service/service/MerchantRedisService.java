package com.gomcc.merchant_checker_service.service;

import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash$;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.gomcc.merchant_checker_service.utility.webClient.AskMilesWebClient;
import com.redis.om.spring.search.stream.EntityStream;
import com.redis.om.spring.search.stream.SearchStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantRedisService {

    private final MerchantRepository merchantRepository;
    private final MerchantRedisHashRepository merchantRedisHashRepository;
    private final EntityStream entityStream;
    private final AskMilesWebClient askMilesWebClient;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String DEV_CACHE_KEY_PREFIX = "dev-merchant:";
    private static final Duration TTL = Duration.ofHours(1);


    public List<MerchantResponseDto> fuzzySearch(String name){

        List<MerchantRedisHash> cachedMerchants = fuzzySearchHashByPattern(name);

        getCacheTtl(cachedMerchants);

        refreshCacheTtl(cachedMerchants);

        getCacheTtl(cachedMerchants);


        if (CollectionUtils.isEmpty(cachedMerchants)){
            log.info("MerchantRedisService::fuzzySearch:: cachedMerchant is empty");
            return List.of();
        }else{
            log.info("MerchantRedisService::fuzzySearch:: cachedMerchant is not empty");
            return cachedMerchants
                    .stream()
                    .map(MerchantResponseDto::fromMerchantRedisHash)
                    .collect(Collectors.toList());
        }
    }


    private List<MerchantRedisHash> fuzzySearchHashByPattern(String name){
        /*
         * Performs regex search `%{word}%`
         * if there are matches, returns list of Merchants that match the regex
         * if no match, returns empty list
         */
        log.info("fuzzySearchHashByPattern:: start");
        SearchStream<MerchantRedisHash> stream = entityStream.of(MerchantRedisHash.class);

        if (name.matches(".*\\s+.*")){
            log.info("name:: {} contains space", name);
            String[] terms = name.trim().split("\\s+");

            for (String term : terms) {
                String fuzzyTerm = "*" + term + "*";
                stream = stream.filter(MerchantRedisHash$.NAME.like(fuzzyTerm));
            }

            return stream.collect(Collectors.toList());

        }else{
//            log.info("name:: {} does not contain space", name);
            String fuzzyTerm = "*" + name + "*";
            log.info("fuzzyTerm:: {}", fuzzyTerm);
            return stream
                    .filter(MerchantRedisHash$.NAME.like(fuzzyTerm))
                    .collect(Collectors.toList());

        }
    }

    private void refreshCacheTtl(List<MerchantRedisHash> cachedMerchants){
        cachedMerchants.forEach(merchantRedisHash -> {
            log.info("refreshCacheTtl:: {} :: {}", merchantRedisHash.getId(), merchantRedisHash.getName());
            redisTemplate.expire(DEV_CACHE_KEY_PREFIX + merchantRedisHash.getId(), TTL);
        });
    }

    private void getCacheTtl(List<MerchantRedisHash> cachedMerchants){
        cachedMerchants.forEach(merchantRedisHash -> {
            Long expiration = redisTemplate.getExpire(DEV_CACHE_KEY_PREFIX + merchantRedisHash.getId());
            log.info("getCacheTtl:: {} :: {} :: {}", merchantRedisHash.getId(), merchantRedisHash.getName(), expiration);
        });
    }
}
