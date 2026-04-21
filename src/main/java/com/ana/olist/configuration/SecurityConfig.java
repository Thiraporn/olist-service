package com.ana.olist.configuration;

import com.ana.common.security.libs.jsonwebtoken.AuthEntryPointJwt;
import com.ana.common.security.libs.jsonwebtoken.AuthTokenFilter;
import com.ana.common.security.libs.jsonwebtoken.CookieConfig;
import com.ana.common.security.libs.logger.LoggingFilter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@ConfigurationProperties(prefix = "requests")
@Data
@RequiredArgsConstructor
public class SecurityConfig {
    private List<String> publicUrls;

    @Bean
    public AuthEntryPointJwt unauthorizedHandler() {
        return new AuthEntryPointJwt();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicUrls.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                 //เพิ่ม LoggingFilter → บันทึกทุก request
                .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}