package com.cand.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();

        })
                .csrf().disable()
                .formLogin(Customizer.withDefaults());
//        http.csrf()
//                .disable()
//                .authorizeHttpRequests()
////                .antMatchers("/swagger-ui/**", "/javainuse-openapi/**")
//                .antMatchers("/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();

//        http.csrf()
//                .disable()
//                .authorizeHttpRequests((authorize) -> {
//                    authorize.anyRequest().authenticated();
//                })
//                .antMatcher("/**")
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .passwordEncoder(e -> encoder.encode(e))
                .username("user")
                .password("password")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}