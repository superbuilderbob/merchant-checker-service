package com.gomcc.merchant_checker_service.webClient;

import com.gomcc.merchant_checker_service.config.WebClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AskMilesWebClientTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void test_connection_expects_4xx(){
        /*
        Test connection to Ask Miles base URL - Expects 405 client side error
         */
        webClient.get()
                .uri(WebClientConfig.MILES_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
