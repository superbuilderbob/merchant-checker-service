package com.gomcc.merchant_checker_service.utility.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AskMilesWebClient {

    private final Duration TimeoutSeconds = Duration.ofSeconds(10);
    private final WebClient webClient;

    public String query(String searchWord){

        Mono<String> response = webClient.get()
                .uri("/search?q={searchWord}", searchWord)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(TimeoutSeconds);

        response.subscribe(data -> {
            System.out.println("Receive response: " + data);

        });

        return response.block();
    }
}
