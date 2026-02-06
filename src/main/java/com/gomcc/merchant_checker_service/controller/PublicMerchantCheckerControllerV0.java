package com.gomcc.merchant_checker_service.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/public/merchant")
public class PublicMerchantCheckerControllerV0 {
    @GetMapping(path = "/list")
    public String getMerchantName(@RequestParam("merchantName") String merchantName){
        return "wassup" + merchantName;
    }
}
