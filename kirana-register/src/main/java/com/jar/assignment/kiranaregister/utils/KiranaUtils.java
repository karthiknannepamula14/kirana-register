package com.jar.assignment.kiranaregister.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jar.assignment.kiranaregister.exception.CustomException;
import com.jar.assignment.kiranaregister.wrapper.KiranaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class KiranaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(KiranaUtils.class);

    private static final ObjectMapper OM = new ObjectMapper();
    private static final Random RANDOM = new Random();
    public static String writeValueAsJsonString(Object object) {
        try {
            return OM.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error(String.format("Exception occurred while converting object : %s to json string ",
                    object.toString()), e);
        }
        return object.toString();
    }
    public static Long generate16DigitRandomNumber() {
        return 1000000000000000L + RANDOM.nextLong();
    }
    public static Double convertCurrencyToINR(Double amountToBeConverted) {
        DecimalFormat df = new DecimalFormat("#.##");
        Double exchangeRate = KiranaWrapper.getInrExchangeRate();
        if (exchangeRate == null || exchangeRate == 0) {
            throw new CustomException("Exchange Rate is not available");
        }
        return new Double(df.format(amountToBeConverted * exchangeRate));
    }
    public static LocalDate convertStringToLocalDate(String currentPattern, String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(currentPattern);
        return LocalDate.parse(date, dtf);
    }
}
