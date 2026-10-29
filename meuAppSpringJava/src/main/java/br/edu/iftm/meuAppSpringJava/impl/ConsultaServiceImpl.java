package br.edu.iftm.meuAppSpringJava.impl;

import java.util.List;
import java.util.Optional;

import br.edu.iftm.meuAppSpringJava.model.Consulta;
import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.repository.ConsultaRepository;
import br.edu.iftm.meuAppSpringJava.repository.pacienteRepository;
import br.edu.iftm.meuAppSpringJava.service.ConsultaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final pacienteRepository pacienteRepository;

    public ConsultaServiceImpl(ConsultaRepository consultaRepository, pacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consulta> getConsultaById(long id) {
        return consultaRepository.findById(id);
    }

    @Override
    @Transactional
    public void saveConsulta(Consulta consulta) {
        Long pacienteId = consulta.getPaciente() == null ? null : consulta.getPaciente().getId();
        if (pacienteId == null) {
            throw new IllegalArgumentException("Selecione um paciente cadastrado");
        }

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        consulta.setPaciente(paciente);
        consultaRepository.save(consulta);
    }

    @Override
    @Transactional
    public void deleteConsultaById(long id) {
        consultaRepository.deleteById(id);
    }
}