package com.gomcc.merchant_checker_service.dto;

import com.gomcc.merchant_checker_service.model.Merchant;
import com.gomcc.merchant_checker_service.model.MerchantModeOfPayment;
import com.gomcc.merchant_checker_service.model.MerchantRedisHash;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MerchantResponseDto implements Serializable{
    private final Long id;
    private final String name;
    private final Long mcc;
    private final String description;
    private final MerchantModeOfPayment mode;

    public static MerchantResponseDto fromMerchant (
            Merchant m
    ){
        return MerchantResponseDto.builder()
                .id(m.getId())
                .name(m.getName())
                .mcc(m.getMcc())
                .description(m.getDescription())
                .mode(m.getMode())
                .build();
    }

    public static MerchantResponseDto fromMerchantRedisHash (
            MerchantRedisHash mrh
    ){
        return MerchantResponseDto.builder()
                .id(mrh.getId())
                .name(mrh.getName())
                .mcc(mrh.getMcc())
                .description(mrh.getDescription())
                .mode(mrh.getMode())
                .build();
    }
}