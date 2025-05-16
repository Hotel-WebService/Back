package com.sw.config;

import java.util.List;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sw.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
	
	  private final CustomUserDetailsService userDetailsService;

	    public SecurityConfig(CustomUserDetailsService uds) {
	        this.userDetailsService = uds;
	    }
	    
	    /*
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration cfg = new CorsConfiguration();
	        cfg.setAllowedOrigins(List.of("http://localhost:3000"));
	        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
	        cfg.setAllowCredentials(true);
	        cfg.setAllowedHeaders(List.of("*"));

	        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
	        src.registerCorsConfiguration("/**", cfg);
	        return src;
	    }    
	    */

	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
	        p.setUserDetailsService(userDetailsService);
	        p.setPasswordEncoder(passwordEncoder());
	        return p;
	    }

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 개발 중에는 CSRF를 꺼두고, 나중에 켜기
        	//.cors().and()
        	.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            
            // 1) 인증 없이 접근 허용할 URL
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/api/userinfo").permitAll()
                .requestMatchers(
                  "/",              // root
                  "/firstpage",     // firstPageController
                  "/error",
                  "/signupPage",	// 회원가입 폼
                  "/signup",
                  "/api/userinfo",// 회원가입 
                  "/login",         // 로그인 폼
                  "/css/**",        // 정적 리소스
                  "/js/**",
                  "/images/**",
                  "/image/**",
                  "/reservationPage",
                  "/api/**"
                ).permitAll()
                .anyRequest().authenticated()  // 그 외는 로그인 필요
            )

            // 2) 폼 로그인
            .formLogin(form -> form
                .loginPage("/login")           // GET /login → 로그인 폼
                .loginProcessingUrl("/login")  // POST /login → 스프링 시큐리티가 처리
                .defaultSuccessUrl("/", true)  // .defaultSuccessUrl("/firstpage", true)
                .failureUrl("/login?error")
                .usernameParameter("loginID")
                .passwordParameter("loginPassword")
                .permitAll()
            )

            // 3) 로그아웃
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration cfg = new CorsConfiguration();
      cfg.setAllowedOrigins(List.of("http://localhost:3000"));  // React 개발서버
      cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
      cfg.setAllowedHeaders(List.of("*"));
      cfg.setAllowCredentials(true);  // Credential(쿠키) 허용

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", cfg);
      return source;
    }
    
}