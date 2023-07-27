package com.study.modoos.common.config;


import com.study.modoos.auth.exception.JwtAccessDeniedHandler;
import com.study.modoos.auth.exception.JwtAuthenticationEntryPoint;
import com.study.modoos.auth.jwt.JwtFilter;
import com.study.modoos.auth.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String API_PREFIX = "/api";
    private final JwtProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtProvider jwtTokenProvider, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling((exceptionHandling) ->
                                exceptionHandling
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) //401 에러 핸들링
                                        .accessDeniedHandler(jwtAccessDeniedHandler)  //403 에러 핸들링
//                                .accessDeniedPage("/errors/access-denied") // 접근 제한 페이지
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.requestMatchers(
                                        API_PREFIX + "/member/join",
                                        API_PREFIX + "/member/nickname-check",
                                        API_PREFIX + "/auth/login",
                                        API_PREFIX + "/auth/email-confirm",
                                        API_PREFIX + "/auth/email-check",
                                        API_PREFIX + "/auth/changePw")
                                .permitAll())
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().authenticated())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtTokenProvider);
    }
}
