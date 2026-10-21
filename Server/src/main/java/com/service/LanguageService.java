package com.service;

import com.models.Language;
import com.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    public List<String> getLanguages() {
        return languageRepository.findAll().stream()
                .map(Language::getName)
                .toList();
    }
    public Language findById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Учитель с id " + id + " не найден"));
    }

}