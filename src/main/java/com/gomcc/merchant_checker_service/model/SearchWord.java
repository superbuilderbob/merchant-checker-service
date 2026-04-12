package com.gomcc.merchant_checker_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

public class SearchWord {

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z\\s]")
    String name;
}
