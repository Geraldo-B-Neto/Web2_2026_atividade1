package com.devWebResolv.mannagerSystem.repository;

import com.devWebResolv.mannagerSystem.model.Tecnologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TecnologiaRepository extends JpaRepository<Tecnologia, Long> {

    Optional<Tecnologia> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT COUNT(p) FROM Projeto p JOIN p.tecnologias t WHERE t.id = :id")
    long countProjetosByTecnologiaId(@Param("id") Long id);
}
