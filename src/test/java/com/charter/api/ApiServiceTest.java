package com.charter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiServiceTest {

    ApiRepository apiRepository;
    ApiService apiService;

    Customer customer1;
    Product product1;
    Product product2;
    Transaction transaction1;
    List<Transaction> transactions;

    @BeforeEach
    public void before() {
        apiRepository = mock(ApiRepository.class);
        apiService = new ApiService();
        apiService.apiRepository = apiRepository;

        customer1 = Customer.builder().id("c-1").name("John").build();
        product1 = Product.builder().id("p-1").name("cart").price(100).build();
        product2 = Product.builder().id("p-2").name("pick").price(200).build();
        transaction1 = Transaction.builder().id("t-1").date(new Date()).customer(customer1).products(Arrays.asList(product1,product2)).build();
        transactions = Arrays.asList(transaction1);
    }

    @Test
    public void testTransactionPoints() throws IOException {
        product1.setPrice(40);
        when(apiRepository.getTransactions()).thenReturn(transactions);

        //49
        product2.setPrice(9);
        Transaction returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 0, "price 49");
        assertEquals(returnedTransaction.getCustomer(), customer1, "correct customer");

        //50
        product2.setPrice(10);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 0, "price 50");

        //51
        product2.setPrice(11);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 1, "price 51");

        //100
        product2.setPrice(60);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 200, "price 100");

        //101
        product2.setPrice(60);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 200, "price 101");

        //151
        product2.setPrice(111);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 201, "price 151");

        //191
        product2.setPrice(151);
        returnedTransaction = apiService.getTransactions().get(0);
        assertEquals(returnedTransaction.getTotalPoints(), 241, "price 191");
    }

    @Test
    public void testCustomerPoints() throws IOException {
        product1.setPrice(101);
        product2.setPrice(51);

        when(apiRepository.getTransactions()).thenReturn(transactions);

        //date now
        transaction1.setDate(new Date());
        CustomerSummary customerSummary = apiService.getCustomerPoints().get(0);
        validate(customerSummary, 202, 0, 0, 0);
        assertEquals(customerSummary.getCustomer(), customer1, "correct customer");

        //date now - 31
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        transaction1.setDate(cal.getTime());
        customerSummary = apiService.getCustomerPoints().get(0);
        validate(customerSummary, 0, 202, 0, 0);

        // date now - 61
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -61);
        transaction1.setDate(cal.getTime());
        customerSummary = apiService.getCustomerPoints().get(0);
        validate(customerSummary, 0, 0, 202, 0);

        // date now - 91
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -91);
        transaction1.setDate(cal.getTime());
        customerSummary = apiService.getCustomerPoints().get(0);
        validate(customerSummary, 0, 0, 0, 202);

    }

    private void validate(CustomerSummary customerSummary, int p30, int p60, int p90, int p90Plus) {
        assertEquals(customerSummary.points30, p30, "points 30");
        assertEquals(customerSummary.points60, p60, "points 60");
        assertEquals(customerSummary.points90, p90, "points 90");
        assertEquals(customerSummary.pointsOver90, p90Plus, "points over 90");
        assertEquals(customerSummary.pointsTotal, 202, "points Total");
    }


}