package com.network;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Object data;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
    public Response(boolean success, Object data) {
        this.success = success;
        this.message = null;
        this.data = data;
    }

}


