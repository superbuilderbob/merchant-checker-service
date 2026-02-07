package com.gomcc.merchant_checker_service.utility.cache;

import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisCacheWarmer implements CommandLineRunner {
    /**
     * Runs immediately after Spring application context if finished
     * Source:
     * <a href="https://removepaywalls.com/https://medium.com/@umeshcapg/preheating-cache-in-spring-boot-applications-on-startup-e01615be774f">...</a>
     */

//    private final MerchantRepository merchantRepository;
    private final MerchantService merchantService;



    @Override
    @Async("taskExecutor")
    public void run(String... args) {
//        List<Merchant> merchants = merchantService.list();
//        merchants.stream().forEach()

    }

}
