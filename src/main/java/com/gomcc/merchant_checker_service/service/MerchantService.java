package com.gomcc.merchant_checker_service.service;

import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public List<Merchant> findAllMerchants() {
        return merchantRepository.findAll();
    }

//    @Cacheable(value="dev-merchant-name", key = "#id")
    public Merchant findMerchantById(Long id) {
        return merchantRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.NOT_FOUND.getErrorCode(),
                        HttpStatus.NOT_FOUND,
                        "The merchant id is not found for id: " + id));

    }
}
