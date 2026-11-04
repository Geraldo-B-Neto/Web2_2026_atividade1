package br.edu.iftm.meuAppSpringJava.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuração de segurança com 3 perfis de acesso:
 *
 * ROLE_USER    -> apenas visualiza pacientes e consultas
 * ROLE_GERENTE -> cria e edita pacientes e consultas (não pode excluir)
 * ROLE_ADMIN   -> acesso total (criar, editar e excluir)
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -----------------------------------------------------------------------
    // Chain 1: API REST (/api/**)
    // -----------------------------------------------------------------------
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {
        http
            .securityMatcher("/api/**")
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider);

        return http.build();
    }

    // -----------------------------------------------------------------------
    // Chain 2: Interface Web (Thymeleaf)
    // -----------------------------------------------------------------------
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(requests -> requests

                // Rotas públicas
                .requestMatchers(
                    "/", "/home", "/login", "/register", "/saveUser",
                    "/accessDenied", "/error", "/css/**",
                    "/swagger-ui/**", "/v3/api-docs*/**"
                ).permitAll()

                // Excluir: somente ADMIN
                .requestMatchers(
                    "/paciente/delete/**",
                    "/consultas/delete/**"
                ).hasAuthority("ROLE_ADMIN")

                // Criar e Editar: GERENTE ou ADMIN
                .requestMatchers(
                    "/paciente/create", "/paciente/save",
                    "/paciente/edit/**", "/paciente/update/**",
                    "/consultas/create", "/consultas/save",
                    "/consultas/edit/**", "/consultas/update/**"
                ).hasAnyAuthority("ROLE_GERENTE", "ROLE_ADMIN")

                // Visualizar pacientes e consultas: qualquer usuário autenticado
                .requestMatchers("/paciente", "/paciente/**", "/consultas", "/consultas/**")
                .hasAnyAuthority("ROLE_USER", "ROLE_GERENTE", "ROLE_ADMIN")

                // Qualquer outra rota: apenas autenticado
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/paciente", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/accessDenied")
            )
            .authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
