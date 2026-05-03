package com.gomcc.merchant_checker_service.model;
import com.gomcc.merchant_checker_service.dto.MerchantResponseDto;
import com.redis.om.spring.annotations.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

    @Document(
            value = "dev-merchant",
            indexName = "dev-merchant-idx",
            timeToLive = 3600L)
    @Getter
    @Setter
    @Builder
    public class MerchantRedisHash {

        @Id
        private Long id;  // Will be the Redis key suffix

        @TextIndexed(sortable = true)  // Creates TEXT index for full-text/fuzzy search
        private String name;

        @Indexed  // Creates TAG index for exact filtering
        private Long mcc;

        @Indexed
        private String description;

        @Indexed
        @Enumerated(EnumType.STRING)
        private MerchantModeOfPayment mode;

        public static MerchantRedisHash fromMerchant(Merchant m){
            return MerchantRedisHash.builder()
                    .id(m.getId())
                    .name(m.getName())
                    .mcc(m.getMcc())
                    .description(m.getDescription())
                    .mode(m.getMode()).build();
        }

        public static MerchantRedisHash fromMerchantResponseDto(MerchantResponseDto mDto){
            return MerchantRedisHash.builder()
                    .id(mDto.getId())
                    .name(mDto.getName())
                    .mcc(mDto.getMcc())
                    .description(mDto.getDescription())
                    .mode(mDto.getMode())
                    .build();
        }
}
