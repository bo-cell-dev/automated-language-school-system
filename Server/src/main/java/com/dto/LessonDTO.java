package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String teacher;
    private String time;
    private String level;
    private String language;
    private String type;
    private String day;
}

