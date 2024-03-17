package com.gridge.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${kakao.url.auth-url}")
    private String KAKAO_AUTH_URL;
    @Value("${kakao.url.api-url}")
    private String KAKAO_API_URL;

    @Bean(value = "kakaoAuthRestClient")
    RestClient restClient(RestClient.Builder builder){
        return builder.baseUrl(KAKAO_AUTH_URL).build();
    }

    @Bean(value = "kakaoApiRestClient")
    RestClient restClient2(RestClient.Builder builder){
        return builder.baseUrl(KAKAO_API_URL).build();
    }
}
