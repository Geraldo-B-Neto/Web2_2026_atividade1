package com.devWebResolv.mannagerSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tecnologias")
public class Tecnologia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 100)
    private String categoria;

    @ManyToMany(mappedBy = "tecnologias")
    @JsonIgnore
    private Set<Projeto> projetos = new HashSet<>();

    public Tecnologia() {
    }

    public Tecnologia(Long id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Tecnologia(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Set<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(Set<Projeto> projetos) {
        this.projetos = projetos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnologia that = (Tecnologia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tecnologia{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
