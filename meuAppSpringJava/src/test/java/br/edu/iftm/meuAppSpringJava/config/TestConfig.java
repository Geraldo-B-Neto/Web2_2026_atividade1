package br.edu.iftm.meuAppSpringJava.config;

import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public pacienteService pacienteService() {
        return Mockito.mock(pacienteService.class);
    }
}