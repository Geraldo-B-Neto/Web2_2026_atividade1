package br.edu.iftm.meuAppSpringJava.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import br.edu.iftm.meuAppSpringJava.config.TestConfig;
import br.edu.iftm.meuAppSpringJava.config.SecurityConfig;
import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(pacienteController.class)
@Import({ TestConfig.class, SecurityConfig.class })
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private pacienteService pacienteService;

    @AfterEach
    void resetMocks() {
        reset(pacienteService);
    }

    // --- Testes de Visualização (ROLE_USER, ROLE_GERENTE, ROLE_ADMIN) ---

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void indexLoadsPacienteListForRegularUser() throws Exception {
        when(pacienteService.getAllPacientes()).thenReturn(List.of(new Paciente()));

        mockMvc.perform(get("/paciente"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/index"))
                .andExpect(model().attributeExists("pacientesList"));
    }

    // --- Testes de Permissão para Usuário Comum (ROLE_USER) ---

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void regularUserCannotAccessCreateForm() throws Exception {
        mockMvc.perform(get("/paciente/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void regularUserCannotSavePaciente() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                        .param("nome", "Paciente A")
                        .param("dataNascimento", "2000-01-01")
                        .param("telefone", "555-1234")
                        .param("email", "paciente@example.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void regularUserCannotDeletePaciente() throws Exception {
        mockMvc.perform(get("/paciente/delete/1"))
                .andExpect(status().isForbidden());
    }

    // --- Testes de Permissão para Gerente (ROLE_GERENTE) ---

    @Test
    @WithMockUser(authorities = "ROLE_GERENTE")
    void gerenteCanAccessCreateForm() throws Exception {
        mockMvc.perform(get("/paciente/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/create"))
                .andExpect(model().attributeExists("paciente"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_GERENTE")
    void gerenteCanSaveValidPaciente() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                        .param("nome", "Paciente A")
                        .param("dataNascimento", "2000-01-01")
                        .param("telefone", "555-1234")
                        .param("email", "paciente@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/paciente"));

        verify(pacienteService).savePaciente(any(Paciente.class));
    }

    @Test
    @WithMockUser(authorities = "ROLE_GERENTE")
    void gerenteCannotDeletePaciente() throws Exception {
        mockMvc.perform(get("/paciente/delete/1"))
                .andExpect(status().isForbidden());
    }

    // --- Testes de Permissão para Administrador (ROLE_ADMIN) ---

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void adminCanDeletePaciente() throws Exception {
        mockMvc.perform(get("/paciente/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/paciente"));

        verify(pacienteService).deletePacienteById(1L);
    }
}
