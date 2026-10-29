package br.edu.iftm.meuAppSpringJava.config;

import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestConfig {

    @Bean
    public pacienteService pacienteService() {
        return Mockito.mock(pacienteService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return Mockito.mock(UserDetailsService.class);
    }
}