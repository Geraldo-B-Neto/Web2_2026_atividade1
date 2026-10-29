package br.edu.iftm.meuAppSpringJava.repository;

import br.edu.iftm.meuAppSpringJava.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}