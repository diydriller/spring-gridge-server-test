package com.gridge.server.config;

import com.gridge.server.controller.BaseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final BaseInterceptor interceptor;

    private final String[] INTERCEPTOR_EXCLUDE_LIST={
            "/member",
            "/check/nickname",
            "/kakao/member",
            "/kakao/callback"
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
