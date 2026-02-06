package com.gomcc.merchant_checker_service.controller;


import com.gomcc.merchant_checker_service.exception.ErrorCode;
import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class PublicMerchantCheckerControllerV0Test {

    @MockitoBean
    private MerchantService merchantService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ExceptionHandlerShouldCatchMissingIdResourceNotFoundException() throws Exception{
        Long nonExistentId = 200L;

        // Stud Merchant service test result
        when(merchantService.findMerchantById(nonExistentId))
                .thenThrow(new ResourceNotFoundException(
                        ErrorCode.NOT_FOUND.getErrorCode(),
                        HttpStatus.NOT_FOUND,
                        "The merchant id is not found for id: " + nonExistentId));


        // Test Merchant service
        mockMvc.perform(get("/public/merchants/{merchantId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("The merchant id is not found for id: " + nonExistentId));
    }
}
