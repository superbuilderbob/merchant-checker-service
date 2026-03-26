package com.gomcc.merchant_checker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.dto.AskMilesResponse;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import com.gomcc.merchant_checker_service.utility.webClient.AskMilesWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    final String CACHE_NAME = "dev-merchant-name:";

    private final MerchantRepository merchantRepository;

    // To Do: Move redisTemplate calls into CacheService
    private final RedisTemplate<String, Merchant> redisTemplate;

    private HashOperations<String, String, Object> hashOperations;


    private final AskMilesWebClient askMilesWebClient;

    private final ObjectMapper mapper;

    public List<Merchant> findAllMerchants() {
        return merchantRepository.findAll();
    }

    public MerchantResponseDto findMerchantById(Long id) {

        String cacheKey = CACHE_NAME + id.toString();
        // if key exists in Redis
        if (redisTemplate.hasKey(cacheKey)){
            log.info("Cache hit for merchant id: {}", cacheKey);
            Merchant m = redisTemplate.opsForValue().get(cacheKey);
            assert m != null;
            MerchantResponseDto dto = new MerchantResponseDto(m.getName(), m.getMcc(), m.getDescription());
            return dto;
        }

        // if key does not exist in Redis
        Optional<Merchant> merchant_from_db = merchantRepository.findById(id);

        if (merchant_from_db.isPresent()){
            redisTemplate.opsForValue().set(cacheKey, merchant_from_db.get()); // dev-merchant-name:1
            hashOperations.put(cacheKey, "body", merchant_from_db); // dev-merchant-name:1 | body | MERCHANT
            return new MerchantResponseDto(
                    merchant_from_db.get().getName(),
                    merchant_from_db.get().getMcc(),
                    merchant_from_db.get().getDescription());
        }else{
            /* Replace this block with try-throw block

            Try: make webclient external call to `ask-miles` endpoint -> saved in db -> cached

            Throw ResourceNotFoundException if `ask-miles` endpoint returns 4/5xx errors
             */

//            String response = askMilesWebClient.query(id.toString());
//            mapper.convertValue(response, Merchant)

            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                    HttpStatus.NOT_FOUND,
                    "The merchant id is not found for id: " + id);
        }
    }

    public List<MerchantResponseDto> fuzzyFindMerchantByName(String name){

        String cacheKey = CACHE_NAME + name;
        // if key exists in Redis
//        if (redisTemplate.hasKey(cacheKey)){
//            log.info("Cache hit for merchant id: {}", cacheKey);
//            Merchant m = redisTemplate.opsForValue().get(cacheKey);
//            assert m != null;
//            MerchantResponseDto dto = new MerchantResponseDto(m.getName(), m.getMcc(), m.getDescription());
//            return dto;
//        }

        return merchantRepository.fuzzyFindMerchantByName("%" + name + "%")
                .filter(result -> !result.isEmpty())
                .orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                HttpStatus.NOT_FOUND,
                "The merchant name <" + name + "> is not found"));
//
//        if (result.isPresent()){
//            return result.get();
//        }else{
//            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
//                    HttpStatus.NOT_FOUND,
//                    "not found");
//        }

        // if key does not exist in Redis

//        if (m.isPresent()){
//            redisTemplate.opsForValue().set(cacheKey, m.get()); // dev-merchant-name::1
//            return new MerchantResponseDto(
//                    m.get().getName(),
//                    m.get().getMcc(),
//                    m.get().getDescription());
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
    }

    public List<PublicAskMilesResponse> getMiles(String searchWord){
        return askMilesWebClient.query(searchWord);
    }
}