package com.jar.assignment.kiranaregister.dto;

import com.jar.assignment.kiranaregister.utils.CurrencyEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionRequest {
    @NotBlank(message = "Customer Id cannot be empty")
    private String customerId;
    @NotBlank(message = "Product Id cannot be empty")
    private final String productId;
    private final String productName;
    private final String productQuantity;
    private String productDescription;
    private String productCategory;
    @NotBlank(message = "Payment method cannot be empty")
    private String paymentMethod; // "cash" or "credit" or "debit"
    @NotNull(message = "Please provide amount.")
    private final Double productAmount;
    @NotNull(message = "Currency cannot be empty")
    private CurrencyEnum currency; // "INR" or "USD"
}
