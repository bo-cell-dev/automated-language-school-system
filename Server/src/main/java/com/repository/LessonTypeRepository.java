package com.repository;

import com.models.Lesson_types;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonTypeRepository extends JpaRepository<Lesson_types, Long> {
    Optional<Lesson_types> findByName(String name);
}