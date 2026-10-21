package com.network;

import com.controller.EditLessonController;
import com.dto.LessonDTO;
import com.request.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object data;
    private Long lessonId;
    private Operation operation;
    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private LessonDTO lessonDTO;
    private EditLessonRequest editLessonRequest;
    private EditLessonRequest2 editLessonRequest2;
    private RegisterToLessonRequest registerToLessonRequest;

    public Request(Operation operation) {
        this.operation = operation;
        this.registrationRequest = null;
        this.loginRequest = null;
    }

    public Request(Operation operation, RegistrationRequest registrationRequest) {
        this.operation = operation;
        this.registrationRequest = registrationRequest;
    }
    public Request(Operation operation, Long lessonId) {
        this.operation = operation;
        this.lessonId = lessonId;
    }

    public Request(Operation operation, LoginRequest loginRequest) {
        this.operation = operation;
        this.loginRequest = loginRequest;
    }

    public Request(Operation operation, EditLessonRequest editLessonRequest) {
        this.operation = operation;
        this.editLessonRequest = editLessonRequest;
    }

    public Request(Operation operation, EditLessonRequest2 editLessonRequest2) {
        this.operation = operation;
        this.editLessonRequest2 = editLessonRequest2;
    }

    public Request(Operation operation, RegisterToLessonRequest registerToLessonRequest) {
        this.operation = operation;
        this.registerToLessonRequest = registerToLessonRequest;
    }
    public LoginRequest getLoginRequest() {
        if (loginRequest != null) {
            return loginRequest;
        }
        if (data instanceof LoginRequest) {
            return (LoginRequest) data;
        }
        return null;
    }

    public Request(Operation operation, Object data) {
        this.operation = operation;
        this.data = data;
    }

}
