package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String login;
    private String password;

    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
