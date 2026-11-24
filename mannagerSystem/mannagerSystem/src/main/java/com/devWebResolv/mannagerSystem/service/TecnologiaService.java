package com.devWebResolv.mannagerSystem.service;

import com.devWebResolv.mannagerSystem.model.Tecnologia;

import java.util.List;
import java.util.Optional;

public interface TecnologiaService {

    List<Tecnologia> listarTodas();

    List<Tecnologia> listarTodos();

    Tecnologia buscarPorId(Long id);

    Optional<Tecnologia> buscarPorIdOptional(Long id);

    Tecnologia salvar(Tecnologia tecnologia);

    void excluir(Long id);

    void deletar(Long id);
}
