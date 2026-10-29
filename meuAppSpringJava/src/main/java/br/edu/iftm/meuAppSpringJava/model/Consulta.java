package br.edu.iftm.meuAppSpringJava.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Selecione um paciente")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "Informe a data e o horário da consulta")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @NotBlank(message = "Informe o motivo da consulta")
    @Size(max = 200, message = "O motivo deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String motivo;

    @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres")
    @Column(length = 2000)
    private String observacoes;

    @NotNull(message = "Informe o valor da consulta")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotBlank(message = "Selecione o status da consulta")
    @Column(nullable = false, length = 20)
    private String status = "AGENDADA";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}