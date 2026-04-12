package com.gomcc.merchant_checker_service.controller;


import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.service.CacheInspectionService;
import com.gomcc.merchant_checker_service.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
        (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PublicMerchantCheckerControllerV0Tests {

    @MockitoBean
    private MerchantService merchantService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ExceptionHandlerShouldCatchMissingIdResourceNotFoundException() throws Exception{
        final String invalidName = "thisisaninvalidname";

//         Stud Merchant service test result
        when(merchantService.fuzzySearch(invalidName))
                .thenThrow(new ResourceNotFoundException(
                        ErrorCode.NOT_FOUND.getErrorCode(),
                        HttpStatus.NOT_FOUND,
                        "Merchant name: " + invalidName + " is not found. Please try again."));


//         Test Merchant service
        mockMvc.perform(get("/public/merchants/name/{merchantName}", invalidName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(
                        String.format("Merchant name: %s is not found. Please try again.", invalidName)));
    }
}
