package com.gomcc.merchant_checker_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AskMilesResponse {
    @JsonProperty("MCC")
    private String mcc;

    @JsonProperty("Store")
    private String store;

    @JsonProperty("Category")
    private String category;

    private String type;
}
