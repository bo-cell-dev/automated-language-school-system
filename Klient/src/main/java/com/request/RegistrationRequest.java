package com.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("имя")
    private String firstName;

    @JsonProperty("фамилия")
    private String lastName;

    @JsonProperty("отчество")
    private String patronymic;

    @JsonProperty("логин")
    private String login;

    @JsonProperty("пароль")
    private String password;
}
