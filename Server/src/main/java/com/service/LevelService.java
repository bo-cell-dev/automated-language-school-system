package com.service;


import com.models.Levels;
import com.models.Teacher;
import com.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    public List<String> getLevels() {
        return levelRepository.findAll().stream()
                .map(Levels::getName)
                .toList();
    }
    public Levels findById(Long id) {
        return levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Уровень с id: " + id + " не найден"));
    }

    public Levels findByName(String name) {
        return levelRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Уровень с именем " + name + " не найден"));
    }
}