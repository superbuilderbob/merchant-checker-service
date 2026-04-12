package com.gomcc.merchant_checker_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.repository.jpa.MerchantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTests {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    @Mock
    private RedisTemplate<String, Merchant> redisTemplate;

    @Mock
    private ValueOperations<String, Merchant> valueOperations;

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Optional<Merchant> generateTestMerchant(){
        /*
        Generate Test Merchant data from dummyMerchant.json

        */
        final ClassPathResource TEST_MERCHANT_DATA = new ClassPathResource("dummyMerchant.json");

        try {
            InputStream inputStream = TEST_MERCHANT_DATA.getInputStream();

            final Merchant TEST_MERCHANT = mapper.readValue(inputStream, Merchant.class);

            return Optional.of(TEST_MERCHANT);

        } catch (Exception e) {
            System.out.println("Something wrong happened" + e);
            return Optional.empty();
        }
    }
}
