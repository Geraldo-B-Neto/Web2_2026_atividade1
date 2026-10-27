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

    @Test
    void indexLoadsPacienteList() throws Exception {
        when(pacienteService.getAllPacientes()).thenReturn(List.of(new Paciente()));

        mockMvc.perform(get("/paciente"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/index"))
                .andExpect(model().attributeExists("pacientesList"));
    }

    @Test
    void createLoadsForm() throws Exception {
        mockMvc.perform(get("/paciente/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/create"))
                .andExpect(model().attributeExists("paciente"));
    }

    @Test
    void saveReturnsFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                        .param("name", "")
                        .param("price", "12.50")
                        .param("stock", "4"))
                .andExpect(status().isOk())
                .andExpect(view().name("paciente/create"))
                .andExpect(model().attributeHasErrors("paciente"));

        verify(pacienteService, never()).savePaciente(any(Paciente.class));
    }

    @Test
    void saveValidPacienteRedirectsToList() throws Exception {
        mockMvc.perform(post("/paciente/save").with(csrf())
                        .param("name", "Paciente A")
                        .param("description", "Descricao")
                        .param("price", "65.24")
                        .param("stock", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/paciente"));

        verify(pacienteService).savePaciente(any(Paciente.class));
    }
}