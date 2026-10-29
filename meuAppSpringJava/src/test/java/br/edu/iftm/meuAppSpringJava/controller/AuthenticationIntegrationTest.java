package br.edu.iftm.meuAppSpringJava.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.edu.iftm.meuAppSpringJava.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

        @Test
        void loginPageIsPublic() throws Exception {
                mockMvc.perform(get("/login"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("user/login"));
        }

    @Test
    void protectedConsultationsRedirectAnonymousUserToLogin() throws Exception {
        mockMvc.perform(get("/consultas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

        @Test
        @WithMockUser
        void authenticatedUserCanRenderConsultationsPage() throws Exception {
                mockMvc.perform(get("/consultas"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("consultas/index"))
                                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                                                .string(org.hamcrest.Matchers.containsString("Sair")));
        }

    @Test
    void registrationStoresEncodedPasswordAndLoginAuthenticates() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerUser"));

        mockMvc.perform(post("/saveUser").with(csrf())
                        .param("name", "Usuário de Teste")
                        .param("email", "TESTE@example.com")
                        .param("password", "SenhaSegura123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        var user = userRepository.findByEmail("teste@example.com").orElseThrow();
        assertTrue(passwordEncoder.matches("SenhaSegura123", user.getPassword()));

        mockMvc.perform(post("/login").with(csrf())
                        .param("email", "TESTE@example.com")
                        .param("password", "SenhaSegura123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/paciente"));
    }

    @Test
    void invalidPasswordReturnsToLoginWithError() throws Exception {
        mockMvc.perform(post("/saveUser").with(csrf())
                        .param("name", "Usuário de Teste")
                        .param("email", "login@example.com")
                        .param("password", "SenhaSegura123"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/login").with(csrf())
                        .param("email", "login@example.com")
                        .param("password", "senha-incorreta"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}