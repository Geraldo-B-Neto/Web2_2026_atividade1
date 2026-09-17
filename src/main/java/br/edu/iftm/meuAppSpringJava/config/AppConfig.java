package br.edu.iftm.meuAppSpringJava.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.edu.iftm.meuAppSpringJava.model.User;
import br.edu.iftm.meuAppSpringJava.repository.UserRepository;

@Configuration
public class AppConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@teste.com";

            if (userRepository.findUserByEmail(email).isEmpty()) {
                User admin = new User();
                admin.setName("Admin Teste");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(List.of("ROLE_ADMIN"));
                userRepository.save(admin);
            }
        };
    }
}