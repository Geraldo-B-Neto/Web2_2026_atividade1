package com.devWebResolv.mannagerSystem.service.impl;

import com.devWebResolv.mannagerSystem.model.Tecnologia;
import com.devWebResolv.mannagerSystem.repository.TecnologiaRepository;
import com.devWebResolv.mannagerSystem.service.TecnologiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TecnologiaServiceImpl implements TecnologiaService {

    private final TecnologiaRepository tecnologiaRepository;

    public TecnologiaServiceImpl(TecnologiaRepository tecnologiaRepository) {
        this.tecnologiaRepository = tecnologiaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tecnologia> listarTodas() {
        return tecnologiaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tecnologia> listarTodos() {
        return listarTodas();
    }

    @Override
    @Transactional(readOnly = true)
    public Tecnologia buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da tecnologia não pode ser nulo.");
        }
        return tecnologiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tecnologia não encontrada com o ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tecnologia> buscarPorIdOptional(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da tecnologia não pode ser nulo.");
        }
        return tecnologiaRepository.findById(id);
    }

    @Override
    @Transactional
    public Tecnologia salvar(Tecnologia tecnologia) {
        if (tecnologia == null) {
            throw new IllegalArgumentException("A tecnologia não pode ser nula.");
        }

        if (tecnologia.getNome() == null || tecnologia.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da tecnologia é obrigatório e não pode ser vazio.");
        }

        String nomeNormalizado = tecnologia.getNome().trim();
        tecnologia.setNome(nomeNormalizado);

        if (tecnologia.getCategoria() != null) {
            tecnologia.setCategoria(tecnologia.getCategoria().trim());
        }

        Optional<Tecnologia> existente = tecnologiaRepository.findByNomeIgnoreCase(nomeNormalizado);
        if (existente.isPresent()) {
            if (tecnologia.getId() == null || !existente.get().getId().equals(tecnologia.getId())) {
                throw new IllegalArgumentException("Já existe uma tecnologia cadastrada com o nome '" + nomeNormalizado + "'.");
            }
        }

        return tecnologiaRepository.save(tecnologia);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da tecnologia não pode ser nulo.");
        }

        Tecnologia tecnologia = buscarPorId(id);

        long vinculos = tecnologiaRepository.countProjetosByTecnologiaId(id);
        if (vinculos > 0 || (tecnologia.getProjetos() != null && !tecnologia.getProjetos().isEmpty())) {
            throw new IllegalStateException("Não é possível excluir a tecnologia '" + tecnologia.getNome()
                    + "' pois ela está vinculada a " + (vinculos > 0 ? vinculos : tecnologia.getProjetos().size()) + " projeto(s).");
        }

        tecnologiaRepository.delete(tecnologia);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        excluir(id);
    }
}
