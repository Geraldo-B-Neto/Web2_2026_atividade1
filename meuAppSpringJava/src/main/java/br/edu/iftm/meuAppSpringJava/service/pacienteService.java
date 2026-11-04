

package br.edu.iftm.meuAppSpringJava.service;

import java.util.List;
import java.util.Optional;

import br.edu.iftm.meuAppSpringJava.model.Paciente;

public interface pacienteService {

    List <Paciente> getAllPacientes();
    Paciente savePaciente(Paciente paciente);
    Optional<Paciente> getPacienteById(long id);
    void deletePacienteById(long id);
}