package br.edu.iftm.meuAppSpringJava.service;

import java.util.List;
import java.util.Optional;

import br.edu.iftm.meuAppSpringJava.model.Consulta;

public interface ConsultaService {

    List<Consulta> getAllConsultas();

    void saveConsulta(Consulta consulta);

    Optional<Consulta> getConsultaById(long id);

    void deleteConsultaById(long id);
}