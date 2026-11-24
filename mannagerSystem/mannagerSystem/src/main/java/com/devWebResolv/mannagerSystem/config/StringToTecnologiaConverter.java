package com.devWebResolv.mannagerSystem.config;

import com.devWebResolv.mannagerSystem.model.Tecnologia;
import com.devWebResolv.mannagerSystem.repository.TecnologiaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTecnologiaConverter implements Converter<String, Tecnologia> {

    private final TecnologiaRepository tecnologiaRepository;

    public StringToTecnologiaConverter(TecnologiaRepository tecnologiaRepository) {
        this.tecnologiaRepository = tecnologiaRepository;
    }

    @Override
    public Tecnologia convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        try {
            Long id = Long.parseLong(source.trim());
            return tecnologiaRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return tecnologiaRepository.findByNomeIgnoreCase(source.trim()).orElse(null);
        }
    }
}
