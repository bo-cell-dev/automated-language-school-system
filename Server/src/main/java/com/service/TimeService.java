package com.service;


import com.models.Time;
import com.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeSlotRepository;

    public List<String> getAvailableTimes() {
        return timeSlotRepository.findAll().stream()
                .map(time -> time.getName().toString())  // преобразуем LocalTime в String
                .toList();
    }

    public Time findById(Long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Время с id: " + id + " не найдено"));
    }


}