package com.gomcc.merchant_checker_service.service;


import com.gomcc.merchant_checker_service.model.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Merchant> redisTemplate;


    public void get(){
        return;
    }

    public void save(){
        return;
    }

    public void hasKey(){
        return;
    }
}
