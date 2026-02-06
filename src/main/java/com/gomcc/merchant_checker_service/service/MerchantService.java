package com.gomcc.merchant_checker_service.services;

import com.gomcc.merchant_checker_service.models.Merchant;
import com.gomcc.merchant_checker_service.repositories.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public List<Merchant> list() {
        return merchantRepository.findAll();
    }
    public List<Merchant> get(String name) {
//        return merchantRepository.findby
    }
}
