package com.charter.api;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/payment")
@Controller @Setter
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    ApiService apiService;

    @GetMapping("/transactions")
    public ResponseEntity getTransactions() {
        try {
            List<Transaction> transactions = apiService.getTransactions();
            return new ResponseEntity(transactions, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Get transactions failed", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customerpoints")
    public ResponseEntity getCustomerPoints() {
        try {
            List<CustomerSummary> customerPoints = apiService.getCustomerPoints();
            return ResponseEntity.ok(customerPoints);
        } catch (Exception e) {
            LOG.error("Get points failed", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
