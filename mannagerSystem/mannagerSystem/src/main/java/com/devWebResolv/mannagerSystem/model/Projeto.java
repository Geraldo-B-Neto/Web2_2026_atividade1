package com.devWebResolv.mannagerSystem.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projetos")
public class Projeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusProjeto status;

    @Column
    private Double orcamento;

    @Column(name = "data_previsao_entrega")
    private LocalDate dataPrevisaoEntrega;

    @ManyToMany
    @JoinTable(
        name = "projeto_tecnologia",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "tecnologia_id")
    )
    private Set<Tecnologia> tecnologias = new HashSet<>();

    public Projeto() {
    }

    public Projeto(Long id, String titulo, String descricao, StatusProjeto status, Double orcamento, LocalDate dataPrevisaoEntrega) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.orcamento = orcamento;
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public Projeto(String titulo, String descricao, StatusProjeto status, Double orcamento, LocalDate dataPrevisaoEntrega) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.orcamento = orcamento;
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public Double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Double orcamento) {
        this.orcamento = orcamento;
    }

    public LocalDate getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public void setDataPrevisaoEntrega(LocalDate dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public Set<Tecnologia> getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(Set<Tecnologia> tecnologias) {
        this.tecnologias = tecnologias;
    }

    public void adicionarTecnologia(Tecnologia tecnologia) {
        this.tecnologias.add(tecnologia);
        tecnologia.getProjetos().add(this);
    }

    public void removerTecnologia(Tecnologia tecnologia) {
        this.tecnologias.remove(tecnologia);
        tecnologia.getProjetos().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projeto projeto = (Projeto) o;
        return Objects.equals(id, projeto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", status=" + status +
                ", orcamento=" + orcamento +
                ", dataPrevisaoEntrega=" + dataPrevisaoEntrega +
                '}';
    }
}
