package com.network;

import com.request.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long lessonId;
    private Operation operation;
    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private Object data;
    private EditLessonRequest editLessonRequest;
    private EditLessonRequest2 editLessonRequest2;
    private RegisterToLessonRequest registerToLessonRequest;


    public Request(Operation operation, Object data) {
        this.operation = operation;
        this.data = data;
    }
    public Request(Operation operation, Long lessonId) {
        this.operation = operation;
        this.lessonId = lessonId;
    }

    // Конструктор для регистрации
    public Request(Operation operation, RegistrationRequest registrationRequest) {
        this.operation = operation;
        this.registrationRequest = registrationRequest;
    }


    public Request(Operation operation, EditLessonRequest2 editLessonRequest2) {
        this.operation = operation;
        this.editLessonRequest2 = editLessonRequest2;
    }

    public Request(Operation operation, LoginRequest loginRequest) {
        this.operation = operation;
        this.loginRequest = loginRequest;
    }
    public Request(Operation operation, RegisterToLessonRequest registerToLessonRequest) {
        this.operation = operation;
        this.registerToLessonRequest = registerToLessonRequest;
    }

    public EditLessonRequest getEditLessonRequest() {
        return editLessonRequest;
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

    public Request(Operation operation, EditLessonRequest editLessonRequest) {
        this.operation = operation;
        this.editLessonRequest = editLessonRequest;
    }

    public Object getData() {
        return data;
    }

    public Operation getOperation() {
        return operation;
    }

    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

}
