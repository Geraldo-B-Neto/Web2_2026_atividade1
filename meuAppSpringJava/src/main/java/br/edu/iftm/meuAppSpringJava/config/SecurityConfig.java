package br.edu.iftm.meuAppSpringJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
        throws Exception {
        return http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/login", "/register", "/saveUser", "/accessDenied", "/css/**").permitAll()
            .anyRequest().authenticated())
        .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("email")
            .defaultSuccessUrl("/paciente", true)
            .failureUrl("/login?error")
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll())
        .exceptionHandling(errors -> errors.accessDeniedPage("/accessDenied"))
        .authenticationProvider(authenticationProvider)
                .build();
    }
}