package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class LevelItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}