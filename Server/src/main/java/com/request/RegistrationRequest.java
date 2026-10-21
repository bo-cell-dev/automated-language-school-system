package com.request;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegistrationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private Long personId;
    private Long roleId;
}
