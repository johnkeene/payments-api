package com.charter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class ApiService {
    private static final Logger LOG = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    ApiRepository apiRepository;

    public List<Transaction> getTransactions() throws IOException {
        List<Transaction> transactions = apiRepository.getTransactions();
        calculatePoints(transactions);
        return transactions;
    }

    public List<CustomerSummary> getCustomerPoints() throws IOException {
        List<Transaction> transactions = apiRepository.getTransactions();
        calculatePoints(transactions);
        List<CustomerSummary> customerSummaries = getCustomerSummaries(transactions);
        return customerSummaries;
    }

    // the following calculations could be moved to a utility class
   private void calculatePoints(List<Transaction> transactions) {
        //calculate points per transaction
        for (Transaction transaction : transactions) {
            int totalPrice = 0;
            for (Product product : transaction.getProducts()) {
                totalPrice += product.getPrice();
            }
            transaction.setTotalPrice(totalPrice);
            transaction.setTotalPoints(calculatePoints(totalPrice));
            LOG.info(MessageFormat.format("Initialize transaction [id {0}, price: {1}, points {2}]",
                    transaction.id, transaction.totalPrice, transaction.totalPoints));
        }
    }
    private int calculatePoints(int totalPrice) {
        int under100 = 0;
        int over100 = 0;
        int over50 = 0;

        under100 = totalPrice % 100;

        if (totalPrice >= 100) {
            over100 = totalPrice - under100;
        }

        if (under100 > 50) {
            over50 = under100 - 50;
        }

        LOG.info(MessageFormat.format("calc points [totalPrice:{0}, under100: {1}, over50: {2}, over100:{3}, points:{4}]",
                totalPrice, under100, over50, over100, (over100 * 2) + over50));
        return (over100 * 2) + over50;
    }

    private List<CustomerSummary> getCustomerSummaries(List<Transaction> transactions) {
        Map<Customer, CustomerSummary> customerSummaries = new HashMap<>();
        for (Transaction transaction : transactions) {
            long days = daysBetween(transaction.getDate());
            Customer customer = transaction.customer;
            CustomerSummary customerSummary = CustomerSummary.builder()
                    .customer(customer)
                    .build();
            LOG.info(MessageFormat.format("days between [transactionId: {0}, customer: {1}, days: {2}, points: {3}, price {4}]",
                    transaction.id, transaction.customer.name, days, transaction.totalPoints, transaction.totalPrice));
            if (days <= 30) {
                customerSummary.setPoints30(customerSummary.getPoints30() + transaction.getTotalPoints());
            } else if (days <= 60) {
                customerSummary.setPoints60(customerSummary.getPoints60() + transaction.getTotalPoints());
            } else if (days <= 90) {
                customerSummary.setPoints90(customerSummary.getPoints90() + transaction.getTotalPoints());
            } else {
                customerSummary.setPointsOver90(customerSummary.getPointsOver90() + transaction.getTotalPoints());
            }
            customerSummary.setPointsTotal(customerSummary.getPointsTotal() + transaction.getTotalPoints());

            CustomerSummary customerSummaryInMap = customerSummaries.get(customer);
            if (customerSummaryInMap != null) {
                customerSummary.mergePoints(customerSummaryInMap);
            }
            customerSummaries.put(customer, customerSummary);
        }
        return new ArrayList<>(customerSummaries.values());
    }

    public long daysBetween(Date date) {
        return ChronoUnit.DAYS.between(date.toInstant(), new Date().toInstant());
    }
}
