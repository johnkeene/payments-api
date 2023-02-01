package com.charter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiControllerTest {
    ApiService apiService;
    ApiController apiController;

    Customer customer1;
    Product product1;
    Product product2;
    Transaction transaction1;
    List<Transaction> transactions;

    CustomerSummary customerSummary1;
    List<CustomerSummary> customerSummaries;


    @BeforeEach
    public void before() {
        apiService = mock(ApiService.class);
        apiController = new ApiController();
        apiController.setApiService(apiService);

        customer1 = Customer.builder().id("c-1").name("John").build();
        product1 = Product.builder().id("p-1").name("cart").price(100).build();
        product2 = Product.builder().id("p-2").name("pick").price(200).build();
        transaction1 = Transaction.builder().id("t-1").date(new Date()).customer(customer1).products(Arrays.asList(product1,product2)).build();
        transactions = Arrays.asList(transaction1);

        customerSummary1 = CustomerSummary.builder().customer(customer1).points30(31).points60(61).points90(91).pointsOver90(181).pointsTotal(361).build();
        customerSummaries = Arrays.asList(customerSummary1);
    }

    @Test
    public void testGetTransactions() throws IOException {
        when(apiService.getTransactions()).thenReturn(transactions);
        ResponseEntity responseEntity = apiController.getTransactions();
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200), "success");
        List<Transaction> returnedTransactions = (List<Transaction>) responseEntity.getBody();
        assertEquals(returnedTransactions.get(0), transaction1, "transaction is equal");

        when(apiService.getTransactions()).thenThrow(new RuntimeException());
        responseEntity = apiController.getTransactions();
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(500), "failed status");
    }

    @Test
    public void getCustomerPoints() throws IOException {
        when(apiService.getCustomerPoints()).thenReturn(customerSummaries);
        ResponseEntity responseEntity = apiController.getCustomerPoints();
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200), "success");
        List<CustomerSummary> returnedCustomerSummaries = (List<CustomerSummary>) responseEntity.getBody();
        assertEquals(returnedCustomerSummaries.get(0), customerSummary1, "customer summary is equal");

        when(apiService.getCustomerPoints()).thenThrow(new RuntimeException());
        responseEntity = apiController.getCustomerPoints();
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(500), "failed status");
    }

}