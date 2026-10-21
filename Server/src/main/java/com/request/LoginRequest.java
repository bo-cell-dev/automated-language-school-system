package com.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String login;
    private String password;

}
