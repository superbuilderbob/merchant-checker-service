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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    final String CACHE_NAME = "dev-merchant-name::";

    private final MerchantRepository merchantRepository;

    // To Do: Move redisTemplate calls into CacheService
    private final RedisTemplate<String, Merchant> redisTemplate;

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
        Optional<Merchant> m = merchantRepository.findById(id);

        if (m.isPresent()){
            redisTemplate.opsForValue().set(cacheKey, m.get()); // dev-merchant-name::1
            return new MerchantResponseDto(
                    m.get().getName(),
                    m.get().getMcc(),
                    m.get().getDescription());
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

    public List<PublicAskMilesResponse> getMiles(String searchWord){
        return askMilesWebClient.query(searchWord);
    }
}