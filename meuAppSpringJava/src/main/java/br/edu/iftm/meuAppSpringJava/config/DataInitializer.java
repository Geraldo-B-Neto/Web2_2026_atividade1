package br.edu.iftm.meuAppSpringJava.config;

import br.edu.iftm.meuAppSpringJava.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *   usuario@teste.com   / senha123  
 *   gerente@teste.com   / senha123  
 *   admin@teste.com     / senha123  
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserServiceImpl userService) {
        return args -> {
            userService.registerWithRoles(
                "Usuário Comum",
                "usuario@teste.com",
                "senha123",
                List.of("ROLE_USER")
            );

            userService.registerWithRoles(
                "Gerente",
                "gerente@teste.com",
                "senha123",
                List.of("ROLE_GERENTE")
            );

            userService.registerWithRoles(
                "Administrador",
                "admin@teste.com",
                "senha123",
                List.of("ROLE_ADMIN")
            );

            System.out.println("=================================================");
            System.out.println("  Usuários de teste inicializados:");
            System.out.println("  usuario@teste.com  / senha123  -> ROLE_USER");
            System.out.println("  gerente@teste.com  / senha123  -> ROLE_GERENTE");
            System.out.println("  admin@teste.com    / senha123  -> ROLE_ADMIN");
            System.out.println("=================================================");
        };
    }
}

