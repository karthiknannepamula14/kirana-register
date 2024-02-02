package com.jar.assignment.kiranaregister.utils;

import org.springframework.beans.factory.annotation.Autowired;

public enum AuthSource {
    MOBILE("Mobile"), INTERNAL("Internal"), WEB("web");

    private final String authSource;

    @Autowired
    AuthSource(String authSource) {
        this.authSource = authSource;
    }

    public String getAuthSource() {
        return authSource;
    }
}
