package com.jar.assignment.kiranaregister.utils;

import com.jar.assignment.kiranaregister.dto.TransactionResponse;
import com.jar.assignment.kiranaregister.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class KiranaPojoConverter {

    public static TransactionResponse getTransactionResponse(Transaction transaction){
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactedProductName(transaction.getProductName())
                .transactionAmountInINR(transaction.getConvertedProductAmountInINR())
                .transactionDate(transaction.getTransactionDate())
                .transactionPaymentMethod(transaction.getPaymentMethod())
                .build();
    }
}
