package com.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private Long id;

    @Expose
    private String name;

    public RoleDTO() {}

    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
