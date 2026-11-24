package com.devWebResolv.mannagerSystem.repository;

import com.devWebResolv.mannagerSystem.model.Projeto;
import com.devWebResolv.mannagerSystem.model.StatusProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByStatus(StatusProjeto status);

    List<Projeto> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT DISTINCT p FROM Projeto p LEFT JOIN FETCH p.tecnologias")
    List<Projeto> findAllWithTecnologias();

    @Query("SELECT p FROM Projeto p LEFT JOIN FETCH p.tecnologias WHERE p.id = :id")
    Optional<Projeto> findByIdWithTecnologias(@Param("id") Long id);
}
