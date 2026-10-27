package br.edu.iftm.meuAppSpringJava.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.iftm.meuAppSpringJava.repository.pacienteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PacienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private pacienteRepository pacienteRepository;

    @Test
    void savePacientePersistsToDatabase() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                        .param("name", "Paciente A")
                        .param("description", "Descricao")
                        .param("price", "65.24")
                        .param("stock", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/paciente"));

        assertTrue(pacienteRepository.findAll().stream()
                .anyMatch(paciente -> "Paciente A".equals(paciente.getName())));
    }
}