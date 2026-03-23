package com.gomcc.merchant_checker_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicAskMilesResponse {
    private String mcc;

    private String name;

    private String description;
}
