package com.charter.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSummaryTest {

    @Test
    void testCustomerSummaryEquals() {
        Customer customerDup1 = Customer.builder().id("1").name("John").build();
        Customer customerDup2 = Customer.builder().id("1").name("John").build();
        Customer customerDiff1 = Customer.builder().id("1").name("Jon").build();
        assertEquals(
                CustomerSummary.builder().customer(customerDup1).build(),
                CustomerSummary.builder().customer(customerDup2).build(), "customer equals");

        assertNotEquals(
                CustomerSummary.builder().customer(customerDup1).build(),
                CustomerSummary.builder().customer(customerDiff1).build(), "customer not equals");

    }

    @Test
    void testCustomerSummaryMerge() {
        CustomerSummary customerSummary1 = CustomerSummary.builder().points30(31).points60(61).points90(91).pointsOver90(181).pointsTotal(264).build();
        CustomerSummary customerSummary2 = CustomerSummary.builder().points30(1).points60(2).points90(3).pointsOver90(4).pointsTotal(5).build();

        customerSummary1.mergePoints(customerSummary2);

        assertEquals(customerSummary1.points30, 32, "points 30");
        assertEquals(customerSummary1.points60, 63, "points 60");
        assertEquals(customerSummary1.points90, 94, "points 90");
        assertEquals(customerSummary1.pointsOver90, 185, "points over 90");
        assertEquals(customerSummary1.pointsTotal, 269, "points total");
    }

}