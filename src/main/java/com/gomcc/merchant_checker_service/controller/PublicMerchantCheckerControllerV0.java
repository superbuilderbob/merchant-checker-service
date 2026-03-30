package com.gomcc.merchant_checker_service.controller;


import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.dto.AskMilesResponse;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import com.gomcc.merchant_checker_service.service.CacheInspectionService;
import com.gomcc.merchant_checker_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path="/public/merchants")
public class PublicMerchantCheckerControllerV0 {

    private final MerchantService merchantService;

    private final CacheInspectionService cacheInspectionService;


    @GetMapping(path = "/name/{merchantName}")
    public ResponseEntity<List<MerchantResponseDto>> fuzzyGetMerchantByName(
            @PathVariable String merchantName) {
        log.info("[fuzzyGetMerchantByName] input {}", merchantName);
        List<MerchantResponseDto> result = merchantService.fuzzyFindMerchantByName(merchantName);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(path = "/miles/{searchWord}")
    public ResponseEntity<List<MerchantRedisHash>> getSearchWord(
            @PathVariable String searchWord

    ) {
            List<MerchantRedisHash> result = merchantService.merchantRedisCacheFuzzySearch(searchWord);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //  ** Below endpoints should be moved to internal endpoints
    @GetMapping(path = "/cache-name")
    public ResponseEntity<String> getCacheName(){
        String cacheName = cacheInspectionService.getCacheNames().toString();
        return ResponseEntity.ok().body(cacheName
                .substring(1, cacheName.length() - 1));
    }

    @GetMapping(path = "/cache-key")
    public ResponseEntity<String> getCacheKey(){
        String cacheName = cacheInspectionService.getCacheNames().toString();
        return ResponseEntity.ok().body("OK");
    }
}
