package br.edu.iftm.meuAppSpringJava.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.iftm.meuAppSpringJava.repository.pacienteRepository;
import br.edu.iftm.meuAppSpringJava.repository.ConsultaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Autowired
    private ConsultaRepository consultaRepository;

    @Test
    @WithMockUser
    void savePacientePersistsToDatabase() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                .param("nome", "Paciente A")
                .param("dataNascimento", "2000-01-01")
                .param("telefone", "555-1234")
                .param("email", "paciente@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/paciente"));

        var paciente = pacienteRepository.findAll().get(0);

        mockMvc.perform(post("/consultas/save").with(csrf())
                .param("paciente.id", paciente.getId().toString())
                .param("dataHora", "2026-11-01T10:30")
                .param("motivo", "Consulta de rotina")
                .param("observacoes", "Retorno em 30 dias")
                .param("valor", "150.00")
                .param("status", "AGENDADA"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/consultas"));

        assertTrue(consultaRepository.findAll().stream()
            .anyMatch(consulta -> "Paciente A".equals(consulta.getPaciente().getNome())));
    }
}