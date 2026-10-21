package com.dto;

import com.models.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public RoleDTO(Role role) {
        if (role != null) {
            this.id = role.getId();
            this.name = role.getName();
        }
    }
}
