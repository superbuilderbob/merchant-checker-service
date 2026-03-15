package com.gomcc.merchant_checker_service.service;

import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    final String CACHE_NAME = "dev-merchant-name";
    private final MerchantRepository merchantRepository;
    private final RedisTemplate<String, Merchant> redisTemplate;

    public List<Merchant> findAllMerchants() {
        return merchantRepository.findAll();
    }

    @Cacheable(value=CACHE_NAME, key = "#id")
    public Merchant findMerchantById(Long id) {

//        if (redisTemplate.hasKey(id.toString())){
//            log.info("Cache hit for merchant id: {}", id.toString());
//            return redisTemplate.opsForValue().get(id.toString());
//        }

        // check cache first before hitting database
        return merchantRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                        HttpStatus.NOT_FOUND,
                        "The merchant id is not found for id: " + id));
    }

}
