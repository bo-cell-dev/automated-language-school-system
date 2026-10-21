package com.controller;

import com.dto.LessonDTO;
import com.dto.ScheduleResponseDTO;
import com.models.*;
import com.request.EditLessonRequest;
import com.repository.*;
import com.request.EditLessonRequest2;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class LessonController {

    private final LessonRepository lessonRepo;
    private final TeacherRepository teacherRepo;
    private final LessonTypeRepository lessonTypeRepo;
    private final TimeRepository timeRepo;
    private final LevelRepository levelRepo;
    private final LanguageRepository languageRepo;
    private final DayRepository dayRepo;

    public String addLesson(EditLessonRequest request) {
        log.info("Получен запрос на добавление урока: {}", request);

        try {
            Teacher teacher = teacherRepo.findByName(request.getTeacher())
                    .orElseGet(() -> {
                        log.warn("Учитель не найден, создаём нового: {}", request.getTeacher());
                        Teacher newTeacher = new Teacher();
                        newTeacher.setName(request.getTeacher());
                        return teacherRepo.save(newTeacher);
                    });

            Lesson_types lessonType = lessonTypeRepo.findByName(request.getLessonType())
                    .orElseGet(() -> {
                        Lesson_types newType = new Lesson_types();
                        newType.setName(request.getLessonType());
                        return lessonTypeRepo.save(newType);
                    });

            LocalTime timeParam = LocalTime.parse(request.getTime());
            Time time = timeRepo.findByName(timeParam)
                    .orElseGet(() -> {
                        Time newTime = new Time();
                        newTime.setName(timeParam);
                        return timeRepo.save(newTime);
                    });

            Levels level = levelRepo.findByName(request.getLevel())
                    .orElseGet(() -> {
                        Levels newLevel = new Levels();
                        newLevel.setName(request.getLevel());
                        return levelRepo.save(newLevel);
                    });

            Language language = languageRepo.findByName(request.getLanguage())
                    .orElseGet(() -> {
                        Language newLang = new Language();
                        newLang.setName(request.getLanguage());
                        return languageRepo.save(newLang);
                    });

            Day day = dayRepo.findByName(request.getDay())
                    .orElseGet(() -> {
                        Day newDay = new Day();
                        newDay.setName(request.getDay());
                        return dayRepo.save(newDay);
                    });


            Lesson lesson = new Lesson();
            lesson.setTeacher(teacher);
            lesson.setLessonType(lessonType);
            lesson.setTime(time);
            lesson.setLevel(level);
            lesson.setLanguage(language);
            lesson.setDay(day);

            lessonRepo.save(lesson);

            log.info("Урок успешно добавлен: {}", lesson);
            return "Урок добавлен";

        } catch (Exception e) {
            log.error("Ошибка при добавлении урока: {}", e.getMessage(), e);
            return "Ошибка при добавлении урока";
        }
    }
    public List<LessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonRepo.findAll();
        return lessons.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private LessonDTO toDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTeacher(lesson.getTeacher().getName());
        dto.setType(lesson.getLessonType().getName());
        dto.setTime(lesson.getTime().getName().toString());
        dto.setLevel(lesson.getLevel().getName());
        dto.setLanguage(lesson.getLanguage().getName());
        dto.setDay(lesson.getDay().getName());
        return dto;
    }
    public String updateLesson(EditLessonRequest2 request) {
        log.info("Получен запрос на обновление урока: {}", request);

        try {
            Lesson lesson = lessonRepo.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Урок с ID " + request.getId() + " не найден"));

            Teacher teacher = teacherRepo.findByName(request.getTeacher())
                    .orElseGet(() -> {
                        Teacher newTeacher = new Teacher();
                        newTeacher.setName(request.getTeacher());
                        return teacherRepo.save(newTeacher);
                    });

            Lesson_types lessonType = lessonTypeRepo.findByName(request.getLessonType())
                    .orElseGet(() -> {
                        Lesson_types newType = new Lesson_types();
                        newType.setName(request.getLessonType());
                        return lessonTypeRepo.save(newType);
                    });

            LocalTime timeParam = LocalTime.parse(request.getTime());
            Time time = timeRepo.findByName(timeParam)
                    .orElseGet(() -> {
                        Time newTime = new Time();
                        newTime.setName(timeParam);
                        return timeRepo.save(newTime);
                    });

            Levels level = levelRepo.findByName(request.getLevel())
                    .orElseGet(() -> {
                        Levels newLevel = new Levels();
                        newLevel.setName(request.getLevel());
                        return levelRepo.save(newLevel);
                    });

            Language language = languageRepo.findByName(request.getLanguage())
                    .orElseGet(() -> {
                        Language newLang = new Language();
                        newLang.setName(request.getLanguage());
                        return languageRepo.save(newLang);
                    });

            Day day = dayRepo.findByName(request.getDay())
                    .orElseGet(() -> {
                        Day newDay = new Day();
                        newDay.setName(request.getDay());
                        return dayRepo.save(newDay);
                    });

            lesson.setTeacher(teacher);
            lesson.setLessonType(lessonType);
            lesson.setTime(time);
            lesson.setLevel(level);
            lesson.setLanguage(language);
            lesson.setDay(day);

            lessonRepo.save(lesson);

            log.info("Урок успешно обновлён: {}", lesson);
            return "Урок обновлён";

        } catch (Exception e) {
            log.error("Ошибка при обновлении урока: {}", e.getMessage(), e);
            return "Ошибка при обновлении урока";
        }
    }

    public String deleteLesson(Long lessonId) {
        try {
            Lesson lesson = lessonRepo.findById(lessonId)
                    .orElseThrow(() -> new IllegalArgumentException("Урок с ID " + lessonId + " не найден"));

            lessonRepo.delete(lesson);
            log.info("Урок успешно удалён: {}", lesson);
            return "Урок удалён";

        } catch (Exception e) {
            log.error("Ошибка при удалении урока: {}", e.getMessage(), e);
            return "Ошибка при удалении урока";
        }
    }
    public ScheduleResponseDTO getSchedule() {
        List<String> days = dayRepo.findAll().stream()
                .map(Day::getName)
                .toList();

        List<String> times = timeRepo.findAll().stream()
                .map(time -> time.getName().toString())
                .toList();

        List<LessonDTO> lessons = lessonRepo.findAll().stream()
                .map(this::toDTO)
                .toList();

        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setDays(days);
        dto.setTimes(times);
        dto.setLessons(lessons);
        return dto;
    }


}
