package com.jar.assignment.kiranaregister.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ExchangeRatesResponse {
    private boolean success;
    private String date;
    private String base;
    private Map<String, Double> rates;
    @JsonCreator
    public ExchangeRatesResponse(@JsonProperty("success") boolean success,
                                 @JsonProperty("date")String date,
                                 @JsonProperty("base")String base,
                                 @JsonProperty("rates")Map<String, Double> rates) {
        this.success = success;
        this.date = date;
        this.base = base;
        this.rates = rates;
    }
}
