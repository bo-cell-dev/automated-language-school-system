package com.service;

import com.models.Lesson_types;
import com.repository.LessonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonTypeService {
    private final LessonTypeRepository lessonTypeRepository;

    public List<String> getLessonTypes() {
        return lessonTypeRepository.findAll().stream()
                .map(Lesson_types::getName)
                .toList();
    }
    public Lesson_types findById(Long id) {
        return lessonTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тип урока с id: " + id + " не найден"));
    }

}