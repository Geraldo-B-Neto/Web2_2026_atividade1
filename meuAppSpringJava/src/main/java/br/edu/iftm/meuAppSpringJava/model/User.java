package br.edu.iftm.meuAppSpringJava.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Informe seu nome")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Informe um e-mail")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
    @Column(name = "user_email", nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "Informe uma senha")
    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
    @Column(name = "user_passwd", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role", nullable = false)
    private List<String> roles = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}