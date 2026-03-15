package com.gomcc.merchant_checker_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheInspectionService {

    private final CacheManager cacheManager;

    public Collection<String> getCacheNames(){
        return cacheManager.getCacheNames();

    }
    public void printCache(String cacheName){
        Cache cache = cacheManager.getCache(cacheName);

    }

    public List<String> getCachekeys(String cacheName){
        Cache cache = cacheManager.getCache(cacheName);

            return List.of("GOOD");
    }
}
