package com.gridge.server.config;

import com.gridge.server.controller.BaseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final BaseInterceptor interceptor;

    private final String[] INTERCEPTOR_EXCLUDE_LIST={
            "/member",
            "/check/nickname",
            "/kakao/member",
            "/kakao/callback",
            "/member/login",
            "/kakao/member/login"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_EXCLUDE_LIST);
    }
}
