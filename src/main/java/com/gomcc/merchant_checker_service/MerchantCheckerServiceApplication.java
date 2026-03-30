package com.gomcc.merchant_checker_service;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "com.gomcc.merchant_checker_service.*")
public class MerchantCheckerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantCheckerServiceApplication.class, args);
	}



}
