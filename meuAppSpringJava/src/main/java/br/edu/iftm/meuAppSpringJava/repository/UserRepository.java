package br.edu.iftm.meuAppSpringJava.repository;

import java.util.Optional;

import br.edu.iftm.meuAppSpringJava.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}