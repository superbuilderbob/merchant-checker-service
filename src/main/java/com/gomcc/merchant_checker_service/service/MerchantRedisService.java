package com.gomcc.merchant_checker_service.service;

import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash$;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.redis.om.spring.search.stream.EntityStream;
import com.redis.om.spring.search.stream.SearchStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantRedisService {

    private final MerchantRepository merchantRepository;
    private final MerchantRedisHashRepository merchantRedisHashRepository;
    private final EntityStream entityStream;

    public List<MerchantResponseDto> fuzzySearch(String name){
        final String fuzzyMerchantPattern = "%" + name + "%";

        List<MerchantRedisHash> cachedMerchant = fuzzySearchHashByPattern(name);

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

            // TODO: Make external query call if db miss
        }
        else{
            log.info("Cache hit - returning cache");

            return cachedMerchant
                    .stream()
                    .map(MerchantResponseDto::fromMerchantRedisHash)
                    .collect(Collectors.toList());
        }

        throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                HttpStatus.NOT_FOUND,
                "Merchant name: " + name + " is not found. Please try again.");

        // TODO: If cache hit -> return result -> refresh cache ttl
    }

    public List<MerchantRedisHash> fuzzySearchHashByPattern(String name){
        /*
         * Performs regex search `%{word}%`
         * if there are matches, returns list of Merchants that match the regex
         * if no match, returns empty list
         */

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
            log.info("name:: {} does not contain space", name);
            return stream
                    .filter(MerchantRedisHash$.NAME.like("*" + name + "*"))
                    .collect(Collectors.toList());

        }

//        final String fuzzyMerchantPattern = "*" + name + "*";
//
//        log.info(fuzzyMerchantPattern);
//
//        // TODO 1: Add regex restriction to name to only allow empty space
//        // TODO 2: Iterate through name pattern split by space
//
//
//        return entityStream
//                .of(MerchantRedisHash.class)
//                .filter(MerchantRedisHash$.NAME.like(fuzzyMerchantPattern))
//                .collect(Collectors.toList());
    }
}
