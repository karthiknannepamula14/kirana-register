package com.jar.assignment.kiranaregister.dao;

import com.jar.assignment.kiranaregister.entity.Transaction;
import com.jar.assignment.kiranaregister.exception.CustomException;
import com.jar.assignment.kiranaregister.repository.TransactionRepository;
import com.jar.assignment.kiranaregister.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionDAO {

    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TransactionDAO(TransactionRepository transactionRepository, MongoTemplate mongoTemplate) {
        this.transactionRepository = transactionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(Transaction document) {
        transactionRepository.save(document);
    }

    public Transaction findByTransactionId(String trasanctionId) {
        return transactionRepository.findByTransactionId(trasanctionId)
                .orElseThrow(() -> new CustomException(String.format("No transaction found for %s"), trasanctionId));
    }

    public List<Transaction> findTransactionsByDate(LocalDate transactionDate) {
        Query query = new Query(Criteria.where("transactionDate").gte(transactionDate.atStartOfDay())
                .lt(transactionDate.plusDays(1).atStartOfDay()));
        return mongoTemplate.find(query, Transaction.class);
    }

    public List<Transaction> findAllTransactionByPagination(Pageable pageable) {
        return transactionRepository.findAll(pageable).getContent();
    }
    public Map<String, List<Transaction>> groupTransactionsBy(Function<Transaction, String> groupByKeyFunction) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        return allTransactions.stream()
                .collect(Collectors.groupingBy(groupByKeyFunction));
    }

}
