package com.jar.assignment.kiranaregister.controller;

import com.jar.assignment.kiranaregister.dto.AuthDto;
import com.jar.assignment.kiranaregister.dto.DailyReport;
import com.jar.assignment.kiranaregister.dto.TransactionRequest;
import com.jar.assignment.kiranaregister.dto.TransactionResponse;
import com.jar.assignment.kiranaregister.entity.Transaction;
import com.jar.assignment.kiranaregister.service.AuthService;
import com.jar.assignment.kiranaregister.service.TransactionService;
import com.jar.assignment.kiranaregister.utils.AuthSource;
import com.jar.assignment.kiranaregister.utils.Constants;
import com.jar.assignment.kiranaregister.utils.KiranaUtils;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final AuthService authService;

    @Autowired
    public TransactionController(TransactionService transactionService, AuthService authService) {
        this.transactionService = transactionService;
        this.authService = authService;
    }

    /**
     * @param transactionId
     * @return The transaction Response for that transaction Id.
     */
    @GetMapping("/api/v1/transactions/{transactionId}")
    public TransactionResponse getTransaction(@PathVariable("transactionId") String transactionId,
                                              final HttpServletRequest request) {
        LOGGER.info("Request received to get a transaction with transaction Id: {}", transactionId);
        AuthDto authDto = AuthDto.builder()
                .authSource(AuthSource.valueOf(request.getHeader("X-SOURCE"))).request(request).build();
        authService.doHeaderKeyAuth(authDto);
        return transactionService.getTransaction(transactionId);
    }

    /**
     * @param page
     * @param size
     * @return The List of all Transactions
     */
    @GetMapping("/api/v1/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                final HttpServletRequest request) {
        LOGGER.info("Request received to get all transactions");
        AuthDto authDto = AuthDto.builder()
                .authSource(AuthSource.valueOf(request.getHeader("X-SOURCE"))).request(request).build();
        authService.doHeaderKeyAuth(authDto);
        Pageable pageable = PageRequest.of(page, size);
        List<Transaction> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * @param transactionRequest The required request to record the entry of the transaction
     *                           Accepts for now both USD and INR Currencies, if USD is in the request it converts
     *                           the value to its corresponding
     *                           INR and Stores in DB
     * @param request
     * @return Required info of the transaction added.
     */

    @PostMapping("/api/v1/transactions/create")
    public ResponseEntity<TransactionResponse> recordTransaction(
            @RequestBody @Valid TransactionRequest transactionRequest, final HttpServletRequest request) {
        LOGGER.info("Request received to record a transaction with request params: {}", transactionRequest);
        AuthDto authDto = AuthDto.builder()
                .authSource(AuthSource.valueOf(request.getHeader("X-SOURCE")))
                .request(request)
                .build();
        authService.doHeaderKeyAuth(authDto);
        return ResponseEntity.ok(transactionService.recordTransaction(transactionRequest));
    }

    /**
     * @param date    The Date for which the user requires the report for.
     *                Input Format: yyyy-MM-dd
     * @param request
     * @return The Response which contains total no of transactions, total amount and the average amount
     * of transactions for that day
     */
    @PostMapping("/api/v1/transactions/report/daily")
    public List<DailyReport> transactionReport(
            @ApiParam(value = "The date (yyyy-MM-dd)", required = true) @RequestParam(name = "date", required = true)
            String date, final HttpServletRequest request) {
        LOGGER.info("Request received to generate a report for the date: {}", date);
        AuthDto authDto = AuthDto.builder()
                .authSource(AuthSource.valueOf(request.getHeader("X-SOURCE"))).request(request).build();
        authService.doHeaderKeyAuth(authDto);
        return transactionService.getDailyReports(KiranaUtils.convertStringToLocalDate(Constants.DATE_FORMAT, date));
    }

    /**
     * @param groupBy The Column Value by which the transactions to be grouped. For now the transactions can we grouped
     *                only by the following : paymentMethod,productCategory,customerId
     * @return List of txn's grouped by the provided value
     */
    @GetMapping("/api/v1/transactions/grouped")
    public Map<String, List<Transaction>> getTransactions(
            @ApiParam(value = "paymentMethod/productCategory/customerId", required = true)
            @RequestParam(name = "groupByValue", required = false) String groupBy, final HttpServletRequest request) {
        LOGGER.info("Request received to retrieve txns grouped by:{}", groupBy);
        AuthDto authDto = AuthDto.builder()
                .authSource(AuthSource.valueOf(request.getHeader("X-SOURCE"))).request(request).build();
        authService.doHeaderKeyAuth(authDto);
        return transactionService.getGroupedTransactions(groupBy);
    }
}
