package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ScheduleResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> days;
    private List<String> times;
    private List<LessonDTO> lessons;
}
