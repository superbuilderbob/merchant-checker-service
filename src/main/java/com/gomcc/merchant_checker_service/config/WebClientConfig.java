package com.gomcc.merchant_checker_service.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {

    public static String MILES_BASE_URL = "https://www.ask-miles.com/api/store";

    @Bean
    public WebClient webClient (WebClient.Builder builder, ClientHttpConnector clientHttpConnector){

        int connectTimeout = 2000; // 2000ms or 2s

        int readTimeout = 6000; // 6000ms or 6s

        return builder
                .baseUrl(MILES_BASE_URL)
                .clientConnector(httpConnector(connectTimeout, readTimeout))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    protected ReactorClientHttpConnector httpConnector(Integer connectTimeout, Integer readTimeout) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                )
        );
    }
}