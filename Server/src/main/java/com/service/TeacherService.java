package com.service;

import com.models.Teacher;
import com.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public List<String> getAllTeachers() {
        // Предположим, что у Teacher есть метод getName()
        return teacherRepository.findAll().stream()
                .map(Teacher::getName)
                .toList();
    }
    public Teacher findById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Преподаватель с id " + id + " не найден"));
    }

    public Teacher findByName(String name) {
        return teacherRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Преподаватель с именем " + name + " не найден"));
    }
}