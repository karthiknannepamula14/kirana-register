package com.jar.assignment.kiranaregister.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse {
    private final String transactionId;
    private final Date transactionDate;
    private final Double transactionAmountInINR;
    private final String transactedProductName;
    private final String transactionPaymentMethod;

    @JsonCreator
    public TransactionResponse(@JsonProperty("transactionId") String transactionId,
                               @JsonProperty("transactionDate") Date  transactionDate,
                               @JsonProperty("transactionAmountInINR") Double transactionAmountInINR,
                               @JsonProperty("transactedProductName") String transactedProductName,
                               @JsonProperty("transactionPaymentMethod") String transactionPaymentMethod) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionAmountInINR = transactionAmountInINR;
        this.transactedProductName = transactedProductName;
        this.transactionPaymentMethod = transactionPaymentMethod;
    }
}
