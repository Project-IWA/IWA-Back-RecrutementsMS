package com.iwa.recrutements.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                .antMatchers("/api/attribution/**").permitAll()
                .anyRequest().authenticated()
        ).httpBasic();
        return http.build();
    }

    /* @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.builder()
            .username("user")
            .password("user")
            .roles("ROLE_USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin")
            .password("admin")
            .roles("ROLE_ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    } */
}
