package com.gomcc.merchant_checker_service.models;

import lombok.Getter;

public enum MerchantModeOfPayment {
    ONLINE_WEB("online"),
    ONLINE_IN_APP("online"),
    OFFLINE_MOBILE_WALLET("offline"),
    OFFLINE_CARD_TAP("offline");

    @Getter
    private final String mode;

    private MerchantModeOfPayment(String mode){
        this.mode = mode;
    }

}
