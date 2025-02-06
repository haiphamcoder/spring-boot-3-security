package com.haiphamcoder.demo.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.haiphamcoder.demo.domain.model.Permission;
import com.haiphamcoder.demo.domain.model.Role;
import com.haiphamcoder.demo.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.haiphamcoder.demo.shared.UnauthorizedAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
        private static final String[] AUTH_WHITELIST = {
                        "/",
                        "/auth/**",
        };

        private static final String ADMIN_ENDPOINT = "/admin/**";

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(unauthorizedAuthenticationEntryPoint))
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers(AUTH_WHITELIST)
                                                .permitAll()
                                                .requestMatchers(ADMIN_ENDPOINT).hasAnyRole(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, ADMIN_ENDPOINT).hasAnyAuthority(Permission.ADMIN_READ.name())
                                                .requestMatchers(HttpMethod.POST, ADMIN_ENDPOINT).hasAnyAuthority(Permission.ADMIN_CREATE.name())
                                                .requestMatchers(HttpMethod.PUT, ADMIN_ENDPOINT).hasAnyAuthority(Permission.ADMIN_UPDATE.name())
                                                .requestMatchers(HttpMethod.DELETE, ADMIN_ENDPOINT).hasAnyAuthority(Permission.ADMIN_DELETE.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
