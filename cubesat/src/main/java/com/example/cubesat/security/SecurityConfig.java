package com.example.cubesat.security;

import com.example.cubesat.model.User;
import com.example.cubesat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/signup").permitAll()
                        .requestMatchers("/api/user/verify-account/**").permitAll()
                        .requestMatchers("/api/user/login/**").permitAll()
                        .requestMatchers("/api/user/reset-password/**").permitAll()
                        .requestMatchers("/api/user/forgot-password/**").permitAll()

                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/ws/device/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("index.html").permitAll()
                        .requestMatchers("index.html/**").permitAll()
                        .requestMatchers("index.html/**").permitAll()

                        .requestMatchers("/").permitAll()
                        .requestMatchers("index.html/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/cubesat/**").authenticated()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().disable())
                .httpBasic();
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("*"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(src);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> opt = userRepository.findByUsername(username);
            if (opt.isEmpty()) {
                throw new BadCredentialsException("User not found");
            }
            User u = opt.get();
            if (!u.isVerified()) {
                throw new BadCredentialsException("Account not verified");
            }
            // Spring Security UserDetails örneği
            return org.springframework.security.core.userdetails.User
                    .withUsername(u.getUsername())
                    .password(u.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}