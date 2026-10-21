package com.repository;

import com.models.Day;
import com.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {
    Optional<Day> findByName(String name);
    List<Day> findAll();

}