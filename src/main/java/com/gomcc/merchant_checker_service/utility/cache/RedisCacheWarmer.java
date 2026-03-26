package com.gomcc.merchant_checker_service.utility.cache;

import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import com.gomcc.merchant_checker_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisCacheWarmer implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Runs immediately after Spring application context if finished
     * Source:
     * <a href="https://removepaywalls.com/https://medium.com/@umeshcapg/preheating-cache-in-spring-boot-applications-on-startup-e01615be774f">...</a>
     */

    private final MerchantRepository merchantRepository;

    private final MerchantService merchantService;

    private final RedisTemplate<String, Merchant> redisTemplate;



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        redisTemplate.opsFor
        List<Merchant> existingMerchants = merchantRepository.findAll();
        existingMerchants.forEach(merchant -> {
                    merchantService.findMerchantById(merchant.getId());
                    System.out.printf("Caching merchant:: %s\n", merchant.getName());
                }
        );
    }
}
