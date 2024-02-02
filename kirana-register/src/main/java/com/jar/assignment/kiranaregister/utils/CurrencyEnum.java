package com.jar.assignment.kiranaregister.utils;

import org.springframework.beans.factory.annotation.Autowired;

public enum CurrencyEnum {
    INR("INR"),USD("USD");

    private final String currencyCode;

    @Autowired
    CurrencyEnum(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
