package com.gomcc.merchant_checker_service.service;

import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.repository.redis.MerchantRedisHashRepository;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MerchantWarmRedisCacheService{
    /**
     * Runs immediately after Spring application context if finished
     * Source:
     * <a href="https://removepaywalls.com/https://medium.com/@umeshcapg/preheating-cache-in-spring-boot-applications-on-startup-e01615be774f">...</a>
     */

    private final MerchantRepository merchantRepository;

    private final MerchantService merchantService;

    private final MerchantRedisHashRepository merchantRedisHashRepository;



    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application is ready - start to warm up redis cache");
//        preheatCache();
    }

    private void preheatCache() {
        // Get all rows in merchant table
        List<Merchant> existingMerchants = merchantRepository.findAll();

        // For each row:
        //      -> Convert Merchant entity to MerchantRedisHash
        //      -> Save to MerchantRedisHashRepository

        existingMerchants.forEach(
                merchant -> {
                    MerchantRedisHash merchantRedishHash = MerchantRedisHash.fromMerchant(merchant);
                    merchantRedisHashRepository.save(merchantRedishHash);
                }
        );
    }
}
