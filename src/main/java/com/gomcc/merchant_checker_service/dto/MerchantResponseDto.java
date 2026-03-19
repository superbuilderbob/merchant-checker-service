package com.gomcc.merchant_checker_service.dto;

import java.io.Serializable;

public record MerchantResponseDto(String name, Long mcc, String description) implements Serializable {

}
