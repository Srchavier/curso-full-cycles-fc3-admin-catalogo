package com.admin.catalogo.domain.validation;

public class Error {

    private String message;

    public Error(String aMessage) {
        this.message = aMessage;
    }

    public String getMessage() {
        return message;
    }
    
}
