package com.grepp.teamnotfound.infra.config;

import com.grepp.teamnotfound.app.model.auth.oauth.CustomOAuth2UserService;
import com.grepp.teamnotfound.infra.auth.AuthenticationEntryPointImpl;
import com.grepp.teamnotfound.infra.auth.oauth2.OAuth2FailureHandler;
import com.grepp.teamnotfound.infra.auth.oauth2.OAuth2SuccessHandler;
import com.grepp.teamnotfound.infra.auth.token.filter.AuthExceptionFilter;
import com.grepp.teamnotfound.infra.auth.token.filter.JwtAuthenticationFilter;
import com.grepp.teamnotfound.infra.auth.token.filter.LogoutFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthExceptionFilter authExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ApplicationContext applicationContext;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint((userInfoEndpointConfig)
                                        -> userInfoEndpointConfig.userService(customOAuth2UserService))
                                .successHandler(applicationContext.getBean(OAuth2SuccessHandler.class))
                                .failureHandler(applicationContext.getBean(OAuth2FailureHandler.class))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        // 로그인/회원가입
                        .requestMatchers(GET, "/api/auth/**").permitAll()
                        .requestMatchers(POST, "/api/auth/**").permitAll()
                        // 소셜로그인 화면 -> 프론트 연동 후 삭제 예정
                        .requestMatchers("/social/login").permitAll()
                        // 오류 페이지 -> 프론트 개발 실제 주소 연동 예정
                        .requestMatchers("/error/**").permitAll()
                        // swagger 관련
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // 프리플라이트 허용
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        // get 특정 게시판의 게시글 리스트 조회 /api/community/articles/v1
                        // get 게시글 좋아요 개수 /api/community/articles/v1/{articleId}/like
                        // get 게시글 상세 조회 /api/community/articles/v1/{articleId}
                        // get 게시글 댓글 개수 /api/community/articles/v1/{articleId}/reply
                        .requestMatchers(GET, "/api/community/articles/v1/**").permitAll()
                        // get /api/profile/v1/users/{userId}
                        // get /api/profile/v1/users/{userId}/pet
                        // get /api/profile/v1/users/{userId}/board
                        .requestMatchers(GET, "/api/profile/v1/users/**").permitAll()
                        .anyRequest().authenticated()
        ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                // .accessDeniedHandler(customAccessDeniedHandler) // 권한 부족 시 호출 (필요한 경우)
        );
        http.addFilterBefore(applicationContext.getBean(LogoutFilter.class), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(Arrays.asList(
                // TODO 프론트 서버로 수정 필요
                "http://localhost:3000",
                "http://localhost:8080",
                "https://mungdiary-172598302113.asia-northeast3.run.app"
        ));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
