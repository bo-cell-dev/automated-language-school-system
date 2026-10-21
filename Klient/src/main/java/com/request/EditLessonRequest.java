package com.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditLessonRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id_teacher")
    private String teacher;

    @JsonProperty("id_lesson_type")
    private String lessonType;

    @JsonProperty("id_time")
    private String time;

    @JsonProperty("id_level")
    private String level;

    @JsonProperty("id_language")
    private String language;

    @JsonProperty("id_day")
    private String day;
}
