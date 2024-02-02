package com.jar.assignment.kiranaregister.entity;

import com.jar.assignment.kiranaregister.utils.Constants;
import com.jar.assignment.kiranaregister.utils.CurrencyEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Document(collection = Constants.TRANSACTION_COLLECTION)
public class Transaction{
    private  String transactionId; //16 Digit Random Number with 'K' appended to it.
    @Indexed(unique = true)
    private  String customerId; //Stores UniqueId of each Customer
    private   String productId;
    private  String productName;
    private  String productQuantity;
    private  String productDescription;
    private  String productCategory;
    private  String paymentMethod; // credit or debit or cash
    private  Double convertedProductAmountInINR;
    private CurrencyEnum currency; // INR or USD
    private Date transactionDate;
}
