package com.gomcc.merchant_checker_service.controller;


import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/public/merchants")
public class PublicMerchantCheckerControllerV0 {

    private final MerchantService merchantService;

    @GetMapping(path = "/{merchantId}")
    public ResponseEntity<Merchant> getMerchantName(@PathVariable Long merchantId) {

        // if path is valid
        Merchant result = merchantService.findMerchantById(merchantId);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
