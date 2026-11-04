
package br.edu.iftm.meuAppSpringJava.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.repository.pacienteRepository;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;


@Service
public class pacienteServiceImpl implements pacienteService {

    @Autowired
    private pacienteRepository pacienteRepository;

    @Override
    public List <Paciente> getAllPacientes(){
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente savePaciente(Paciente paciente){
        return this.pacienteRepository.save(paciente);
    }

    @Override
    public Optional<Paciente> getPacienteById(long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public void deletePacienteById(long id) {
        this.pacienteRepository.deleteById(id);
    }

}
