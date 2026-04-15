package com.edigest.journalApp.config;

import com.edigest.journalApp.filter.JwtFilter;
import com.edigest.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Yeh dono lines aapne galti se hata di thi
    @Autowired
    private JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // authorizeHttpRequests ki jagah authorizeRequests aayega
                .authorizeRequests(auth -> auth
                        // requestMatchers ki jagah antMatchers aayega
                        .antMatchers("/public/**").permitAll()
                        .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .antMatchers("/user/**", "/journal/**").authenticated()
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(httpBasic -> {})
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ NEW WAY
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

