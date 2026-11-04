package br.edu.iftm.meuAppSpringJava.impl;

import java.util.List;
import java.util.Locale;

import br.edu.iftm.meuAppSpringJava.model.User;
import br.edu.iftm.meuAppSpringJava.repository.UserRepository;
import br.edu.iftm.meuAppSpringJava.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(User user) {
        String normalizedEmail = user.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Novos usuários cadastrados pelo formulário recebem ROLE_USER
        user.setRoles(List.of("ROLE_USER"));
        userRepository.save(user);
    }

    /**
     * Cria um usuário com roles específicas (usado pelo DataInitializer).
     */
    @Transactional
    public void registerWithRoles(String name, String email, String password, List<String> roles) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            return; // Já existe, não recria
        }
        User user = new User();
        user.setName(name);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
    }
}
