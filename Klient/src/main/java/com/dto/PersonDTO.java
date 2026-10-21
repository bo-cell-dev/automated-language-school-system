package com.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private Long id;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String middleName;

    public PersonDTO() {}

    public PersonDTO(Long id, String firstName, String lastName, String middleName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

}
