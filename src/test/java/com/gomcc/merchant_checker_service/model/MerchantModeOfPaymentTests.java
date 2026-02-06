package com.gomcc.merchant_checker_service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MerchantModeOfPaymentTest {
    /*
    * Test MerchantModeOfPayment Enum
    */
    // From {https://pradeesh-kumar.medium.com/parameterized-test-in-junit-85e7daec677b}
    private static Stream<Arguments> merchantModeOfPaymentEnumSource(){

        return Stream.of(
                Arguments.of(MerchantModeOfPayment.ONLINE_WEB, "online"),
                Arguments.of(MerchantModeOfPayment.ONLINE_IN_APP, "online"),
                Arguments.of(MerchantModeOfPayment.ONLINE_SHOPBACKPAY, "online"),
                Arguments.of(MerchantModeOfPayment.ONLINE_FAVEPAY, "online"),
                Arguments.of(MerchantModeOfPayment.ONLINE_KRISPAY, "online"),
                Arguments.of(MerchantModeOfPayment.OFFLINE_MOBILE_WALLET, "offline"),
                Arguments.of(MerchantModeOfPayment.OFFLINE_PHYSICAL_CARD, "offline")
        );
    }

    @ParameterizedTest
    @MethodSource("merchantModeOfPaymentEnumSource")
    void testMerchantModeOfPaymentReturnExpectedMode(MerchantModeOfPayment input, String expected){
        /*
        * Given a MerchantModeOfPayment enum as input, should return the correct corresponding mode.
        */
        Assertions.assertEquals(expected, input.getMode());
    }

}
