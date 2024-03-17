package com.gridge.server.service.sns;

import com.gridge.server.service.sns.dto.KakaoAccountInfo;
import com.gridge.server.service.sns.dto.KakaoToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class KakaoRestClientService {
    private final RestClient kakaoAuthRestClient;
    private final RestClient kakaoApiRestClient;

    @Value("${kakao.key.rest-api-key}")
    private String KAKAO_REST_API_KEY;
    @Value("${kakao.key.client-secret}")
    private String KAKAO_SECRET;
    @Value("${kakao.url.redirect-url}")
    private String KAKAO_REDIRECT_URL;

    public KakaoRestClientService(@Qualifier("kakaoAuthRestClient") RestClient kakaoAuthRestClient,
                              @Qualifier("kakaoApiRestClient") RestClient kakaoApiRestClient) {
        this.kakaoApiRestClient = kakaoApiRestClient;
        this.kakaoAuthRestClient = kakaoAuthRestClient;
    }

    public String getAccessToken(String authorizationCode) {
        return Objects.requireNonNull(kakaoAuthRestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", KAKAO_REST_API_KEY)
                        .queryParam("redirect_uri", KAKAO_REDIRECT_URL)
                        .queryParam("code", authorizationCode)
                        .queryParam("client_secret", KAKAO_SECRET)
                        .build())
                .contentType(APPLICATION_FORM_URLENCODED)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(KakaoToken.class)).getAccess_token();
    }

    public KakaoAccountInfo.Account getKakaoInfo(String accessToken) {
        return Objects.requireNonNull(kakaoApiRestClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .header("content-type", "application/x-www-form-urlencoded")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(KakaoAccountInfo.class)).getKakao_account();
    }
}
