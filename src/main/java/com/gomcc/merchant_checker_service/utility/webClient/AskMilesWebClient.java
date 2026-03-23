package com.gomcc.merchant_checker_service.utility.webClient;

import com.gomcc.merchant_checker_service.dto.AskMilesResponse;
import com.gomcc.merchant_checker_service.dto.PublicAskMilesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AskMilesWebClient {

    private final WebClient webClient;

    public List<PublicAskMilesResponse> query(String searchWord){

        Mono<List<PublicAskMilesResponse>> response = webClient.get()
                .uri("/search?q={searchWord}", searchWord)
                .retrieve()
                .bodyToMono(
                        new ParameterizedTypeReference<List<AskMilesResponse>>(){}
                )
                .map(data -> data.stream().map(
                        d -> PublicAskMilesResponse.builder()
                                .mcc(d.getMcc())
                                .name(d.getStore())
                                .description(d.getCategory())
                                .build()).toList()
                );


        response.subscribe(data -> {
            System.out.println("Receive response: " + data);

        });

        return response.block();
    }
}
