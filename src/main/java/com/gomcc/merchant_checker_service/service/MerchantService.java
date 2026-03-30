package com.gomcc.merchant_checker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash$;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.utility.webClient.AskMilesWebClient;
import com.redis.om.spring.search.stream.EntityStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    final String CACHE_NAME = "dev-merchant-name:";

    private final MerchantRepository merchantRepository;

    private final MerchantRedisHashRepository merchantRedisHashRepository;

    // To Do: Move redisTemplate calls into CacheService
    private final RedisTemplate<String, Merchant> redisTemplate;

    private HashOperations<String, String, Object> hashOperations;


    private final AskMilesWebClient askMilesWebClient;

    private final ObjectMapper mapper;

    private final EntityStream entityStream;

    public List<Merchant> findAllMerchants() {
        return merchantRepository.findAll();
    }

//    public MerchantResponseDto findMerchantById(Long id) {
//
//        String cacheKey = CACHE_NAME + id.toString(); //dev-merchant-name:1
//        // if key exists in Redis
////        if (redisTemplate.hasKey(cacheKey)){
////            log.info("Cache hit for merchant id: {}", cacheKey);
////            Merchant m = redisTemplate.opsForValue().get(cacheKey);
////            assert m != null;
////            MerchantResponseDto dto = new MerchantResponseDto(m.getName(), m.getMcc(), m.getDescription());
////            return dto;
////        }
//
//
//        // if key does not exist in Redis
//        Optional<Merchant> merchant_from_db = merchantRepository.findById(id); //Merchant
//
//        if (merchant_from_db.isPresent()){
//            redisTemplate.opsForValue().set(cacheKey, merchant_from_db.get()); // dev-merchant-name:1
////            return new MerchantResponseDto(
////                    merchant_from_db.get().getName(),
////                    merchant_from_db.get().getMcc(),
////                    merchant_from_db.get().getDescription());
//        }else{
//            /* Replace this block with try-throw block
//
//            Try: make webclient external call to `ask-miles` endpoint -> saved in db -> cached
//
//            Throw ResourceNotFoundException if `ask-miles` endpoint returns 4/5xx errors
//             */
//
////            String response = askMilesWebClient.query(id.toString());
////            mapper.convertValue(response, Merchant)
//
//            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
//                    HttpStatus.NOT_FOUND,
//                    "The merchant id is not found for id: " + id);
//        }
//    }

    public List<MerchantResponseDto> fuzzyFindMerchantByName(String name){
        final String fuzzyMerchantPattern = "%" + name + "%";

        List<MerchantRedisHash> cachedMerchant = merchantRedisCacheFuzzySearch(name);

        if (CollectionUtils.isEmpty(cachedMerchant)){
            // If cache miss -> query db -> save to cache with 24 hours ttl -> return result
            log.info("Cache miss - querying db");
            List<MerchantResponseDto> dbMerchants = merchantRepository
                    .fuzzyQueryMerchantByName(fuzzyMerchantPattern)
                    .stream()
                    .map(MerchantResponseDto::fromMerchant)
                    .collect(Collectors.toList());

            log.info("Querying db complete");

            if (!CollectionUtils.isEmpty(dbMerchants)){
                log.info("Query result from db, {}", dbMerchants.size());
                dbMerchants
                        .stream()
                        .map(MerchantRedisHash::fromMerchantResponseDto)
                        .forEach(merchantRedisHashRepository::save);
                log.info("Db results cached");

                return dbMerchants;
            }

        }else{
            log.info("Cache hit - returning cache");

            return cachedMerchant
                    .stream()
                    .map(MerchantResponseDto::fromMerchantRedisHash)
                    .collect(Collectors.toList());
        }

        throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                HttpStatus.NOT_FOUND,
                "Merchant name: " + name + " is not found. Please try again.");
        // If cache hit -> return result -> refresh cache ttl
    }

    public List<MerchantRedisHash> merchantRedisCacheFuzzySearch(String name){
        /*
         * Performs regex search `%{word}%`
         * if there are matches, returns list of Merchants that match the regex
         * if no match, returns empty list
         */
        final String fuzzyMerchantPattern = "*" + name + "*";

        return entityStream
                .of(MerchantRedisHash.class)
                .filter(MerchantRedisHash$.NAME.like(fuzzyMerchantPattern))
                .collect(Collectors.toList());
    }

    public List<PublicAskMilesResponse> getMiles(String searchWord){
        return askMilesWebClient.query(searchWord);
    }
}