

package br.edu.iftm.meuAppSpringJava.service;

import java.util.List;

import br.edu.iftm.meuAppSpringJava.model.Paciente;

public interface pacienteService {

    List <Paciente> getAllPacientes();
    void savePaciente(Paciente paciente );
    Paciente getPacienteById(long id);
    void deletePacienteById(long id);
}