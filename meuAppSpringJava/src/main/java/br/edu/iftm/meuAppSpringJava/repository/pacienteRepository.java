package br.edu.iftm.meuAppSpringJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.meuAppSpringJava.model.Paciente;

@Repository
public interface pacienteRepository extends JpaRepository<Paciente, Long> {

}