package com.gomcc.merchant_checker_service.service;


import com.gomcc.merchant_checker_service.exception.ResourceNotFoundException;
import com.gomcc.merchant_checker_service.repository.MerchantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("Should throw ResourceNotFoundException if merchantId is not found")
    void NonExistentIdShouldThrowResourceNotFoundException() {

        // Given
        final Long invalidMerchantId = 200L;
        // stud merchant repository to return Optional.empty()

        // When
        when(merchantRepository.findById(invalidMerchantId)).thenReturn(Optional.empty());

        // Then

        // #1 throws ResourceNotFoundException
        final ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> merchantService.findMerchantById(invalidMerchantId)
        );
        // #2 returns correct error message
        assertEquals("The merchant id is not found for id: " + invalidMerchantId,
                exception.getMessage());

        // #3 called merchantRepository only once
        verify(merchantRepository, times(1)).findById(invalidMerchantId);
    }
}
