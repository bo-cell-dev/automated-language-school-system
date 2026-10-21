package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserConnectionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String status;
}
