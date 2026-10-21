package com.request;

import com.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterToLessonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long lessonId;
    private UserDTO user;

}