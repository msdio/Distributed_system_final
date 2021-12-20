package com.example.cdstermproject;

public class USER {

    String email;
    String message;

    public USER(String email, String message) {
        this.email = email;
        this.message = message;

    }

    public String getName() {
        return email;
    }

    public void setName(String name) {
        this.email = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

