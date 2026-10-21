package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegistrationDTO {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
}
