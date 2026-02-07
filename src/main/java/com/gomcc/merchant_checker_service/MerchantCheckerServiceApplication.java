package com.gomcc.merchant_checker_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableCaching
public class MerchantCheckerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantCheckerServiceApplication.class, args);
	}



}
