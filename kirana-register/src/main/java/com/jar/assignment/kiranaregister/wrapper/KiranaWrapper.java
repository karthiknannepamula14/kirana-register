package com.jar.assignment.kiranaregister.wrapper;

import com.jar.assignment.kiranaregister.dto.ExchangeRatesResponse;
import com.jar.assignment.kiranaregister.exception.CustomException;
import com.jar.assignment.kiranaregister.utils.KiranaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.Objects;

@Component
public class KiranaWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(KiranaWrapper.class);
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String UPDATED_EXCHANGE_RATE_END_POINT = "https://api.fxratesapi.com/latest";
    public static Double getInrExchangeRate() {
        ExchangeRatesResponse response =
                httpRequestExecutor(UPDATED_EXCHANGE_RATE_END_POINT, ExchangeRatesResponse.class);
        if (response != null && response.getRates() != null) {
            return response.getRates().get("INR");
        }
        return null;
    }

    private static <T> T httpRequestExecutor(String url, Class<T> response) throws CustomException {
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, response);
            if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(responseEntity.getBody())) {
                LOGGER.info("Received response  with response body : {}",
                        KiranaUtils.writeValueAsJsonString(responseEntity));
                return responseEntity.getBody();
            }
            LOGGER.info("Received response from the api with response body : {}", responseEntity);
        } catch (HttpClientErrorException e) {
            LOGGER.error(String.format("HttpClientErrorException occurred with response: %s while sending request",
                    e.getResponseBodyAsString()), e);
            throw new CustomException("4xx", "Client Exception Occurred");
        } catch (HttpServerErrorException e) {
            LOGGER.error(String.format("HttpServerErrorException occurred with response: %s while sending request",
                    e.getResponseBodyAsString()), e);
            throw new CustomException("5xx", "Server Exception Occurred");
        } catch (Exception e) {
            LOGGER.error("Exception occurred  while sending while sending request", e);
        }
        throw new CustomException("800", "Something Went Wrong!!");
    }

}
