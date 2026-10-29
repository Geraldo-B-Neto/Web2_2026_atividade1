package br.edu.iftm.meuAppSpringJava.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do paciente")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Informe a data de nascimento")
    @Past(message = "A data de nascimento deve estar no passado")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "Informe o telefone")
    @Size(max = 30, message = "O telefone deve ter no máximo 30 caracteres")
    @Column(nullable = false, length = 30)
    private String telefone;

    @NotBlank(message = "Informe o e-mail")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}