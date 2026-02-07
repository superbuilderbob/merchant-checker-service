package com.gomcc.merchant_checker_service.model;

import lombok.Getter;

public enum MerchantModeOfPayment {
    ONLINE_WEB("online"),
    ONLINE_IN_APP("online"),
    ONLINE_SHOPBACKPAY("online"),
    ONLINE_FAVEPAY("online"),
    ONLINE_KRISPAY("online"),
    OFFLINE_MOBILE_WALLET("offline"),
    OFFLINE_PHYSICAL_CARD("offline");

    @Getter
    private final String mode;

    MerchantModeOfPayment(String mode){
        this.mode = mode;
    }

}
