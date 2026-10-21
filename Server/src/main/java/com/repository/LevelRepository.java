package com.repository;

import com.models.Levels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Levels, Long> {
    Optional<Levels> findByName(String name);
}
