package com.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class EditLessonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String teacher;
    private String lessonType;
    private String time;
    private String level;
    private String language;
    private String day;
}
