package com.cand.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Function;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                }) // all requests should be authenticated
                // csrf must also be disabled for token (JWT, API-KEY) based authentication as each request will be validated by token filter!!
//                .csrf().disable() // for POST, PUT, DELETE if enabled these methods will give 403
//                .formLogin(Customizer.withDefaults()); // Shows a login page
                .httpBasic(Customizer.withDefaults()); // a login web page pop-up is shown for not authenticated request

        http.headers().frameOptions().disable(); // for h2-console to work
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .passwordEncoder(s -> encoder.encode(s))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}