package com.jar.assignment.kiranaregister.service;

import com.jar.assignment.kiranaregister.dao.TransactionDAO;
import com.jar.assignment.kiranaregister.dto.DailyReport;
import com.jar.assignment.kiranaregister.dto.TransactionRequest;
import com.jar.assignment.kiranaregister.dto.TransactionResponse;
import com.jar.assignment.kiranaregister.entity.Transaction;
import com.jar.assignment.kiranaregister.utils.CurrencyEnum;
import com.jar.assignment.kiranaregister.utils.KiranaPojoConverter;
import com.jar.assignment.kiranaregister.utils.KiranaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final KiranaPojoConverter kiranaPojoConverter;

    @Autowired
    public TransactionService(TransactionDAO transactionDAO, KiranaPojoConverter kiranaPojoConverter) {
        this.transactionDAO = transactionDAO;
        this.kiranaPojoConverter = kiranaPojoConverter;
    }

    public TransactionResponse recordTransaction(TransactionRequest transactionRequest) {
        double convertedAmount =
                CurrencyEnum.USD.equals(transactionRequest.getCurrency()) ? KiranaUtils.convertCurrencyToINR(
                        transactionRequest.getProductAmount()) : transactionRequest.getProductAmount();
        Transaction transaction = Transaction.builder()
                .transactionId("K"+KiranaUtils.generate16DigitRandomNumber())
                .customerId(transactionRequest.getCustomerId())
                .productId(transactionRequest.getProductId())
                .productName(transactionRequest.getProductName())
                .productQuantity(transactionRequest.getProductQuantity())
                .productDescription(transactionRequest.getProductDescription())
                .productCategory(transactionRequest.getProductCategory())
                .paymentMethod(transactionRequest.getPaymentMethod())
                .convertedProductAmountInINR(convertedAmount)
                .currency(transactionRequest.getCurrency())
                .transactionDate(Date.from(Instant.now()))
                .build();
        transactionDAO.save(transaction);
        return KiranaPojoConverter.getTransactionResponse(transaction);
    }

    public TransactionResponse getTransaction(String transactionId) {
        Transaction transaction = transactionDAO.findByTransactionId(transactionId);
        return KiranaPojoConverter.getTransactionResponse(transaction);
    }

    public List<DailyReport> getDailyReports(LocalDate date) {
        List<Transaction> transactions = transactionDAO.findTransactionsByDate(date);
        DailyReport dailyReport = calculateDailyReport(transactions, date);
        return List.of(dailyReport);
    }

    private DailyReport calculateDailyReport(List<Transaction> transactions, LocalDate date) {
        DecimalFormat df = new DecimalFormat("#.##");
        double totalAmount = transactions.stream().mapToDouble(Transaction::getConvertedProductAmountInINR).sum();
        return DailyReport.builder()
                .totalAmountInINR(totalAmount)
                .totalTransactions(transactions.size())
                .averageAmountInINR(new Double(df.format(transactions.size() > 0 ? totalAmount / transactions.size() : 0)))
                .date(date).build();
    }

    public List<Transaction> getAllTransactions(Pageable pageable){
        return transactionDAO.findAllTransactionByPagination(pageable);
    }
    public Map<String, List<Transaction>> getGroupedTransactions(String groupByValue){
        Function<Transaction, String> groupByKeyFunction = null;
        switch (groupByValue) {
            case "paymentMethod":
                groupByKeyFunction = Transaction::getPaymentMethod;
                break;
            case "productCategory":
                groupByKeyFunction = Transaction::getProductCategory;
                break;
            case "customerId":
                groupByKeyFunction = Transaction::getCustomerId;
                break;
        }
        return transactionDAO.groupTransactionsBy(groupByKeyFunction);
    }
}
