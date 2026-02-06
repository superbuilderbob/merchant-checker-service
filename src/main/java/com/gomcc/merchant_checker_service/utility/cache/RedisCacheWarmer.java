package com.gomcc.merchant_checker_service.utilities.cache;

import com.gomcc.merchant_checker_service.models.Merchant;
import com.gomcc.merchant_checker_service.repositories.MerchantRepository;
import com.gomcc.merchant_checker_service.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    @Async
    public void run(String... args) {
        List<Merchant> merchants = merchantService.list();
//        merchants.stream().forEach()

    }

}
