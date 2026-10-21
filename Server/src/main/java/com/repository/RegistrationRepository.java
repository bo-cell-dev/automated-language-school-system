package com.repository;

import com.models.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Registration> findByLessonIdAndUserId(Long lessonId, Long userId);
    List<Registration> findByLessonId(Long lessonId);

}


