package com.sw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    /*
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // React 개발 서버 주소
        cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // 허용 HTTP 메서드
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        // 자격증명(Cookie 등) 허용
        cfg.setAllowCredentials(true);
        // 허용 헤더
        cfg.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","X-Requested-With"));

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
    */
}
