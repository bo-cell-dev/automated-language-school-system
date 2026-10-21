package com.service;

import com.dto.LessonDTO;
import com.models.*;
import com.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final TeacherRepository teacherRepo;
    private final TimeRepository timeRepo;
    private final LanguageRepository languageRepo;
    private final LevelRepository levelRepo;
    private final LessonTypeRepository lessonTypeRepo;
    private final LessonRepository lessonRepository;

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void addLesson(LessonDTO lessonDTO) {
        Teacher teacher = teacherRepo.findByName(lessonDTO.getTeacher())
                .orElseThrow(() -> new RuntimeException("Учитель '" + lessonDTO.getTeacher() + "' не найден"));

        // Преобразуем строку времени в LocalTime
        LocalTime localTime = LocalTime.parse(lessonDTO.getTime(), DateTimeFormatter.ofPattern("HH:mm"));

        Time time = timeRepo.findByName(localTime)
                .orElseThrow(() -> new RuntimeException("Время '" + lessonDTO.getTime() + "' не найдено"));

        Levels level = levelRepo.findByName(lessonDTO.getLevel())
                .orElseThrow(() -> new RuntimeException("Уровень '" + lessonDTO.getLevel() + "' не найден"));

        Lesson_types lessonType = lessonTypeRepo.findByName(lessonDTO.getType())
                .orElseThrow(() -> new RuntimeException("Тип урока '" + lessonDTO.getType() + "' не найден"));

        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setTime(time);
        lesson.setLevel(level);
        lesson.setLessonType(lessonType);

        lessonRepository.save(lesson);
    }
    public Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Учитель с id " + id + " не найден"));
    }

}
