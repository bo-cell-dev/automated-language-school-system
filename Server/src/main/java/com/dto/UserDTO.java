package com.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private Long id;

    @Expose
    private String login;

    @Expose
    private String password;

    @Expose
    private PersonDTO person;

    @Expose
    private RoleDTO role;

    // Конструктор, который принимает все поля
    public UserDTO(Long id, String login, String password, PersonDTO person, RoleDTO role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.person = person;
        this.role = role;
    }

    public UserDTO() {
    }
}
