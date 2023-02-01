package com.charter.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerEquals() {
        assertEquals(
                Customer.builder().id("1").name("John").build(),
                Customer.builder().id("1").name("John").build(), "customer equals");

        assertNotEquals(
                Customer.builder().id("1").name("John").build(),
                Customer.builder().id("1").name("Jon").build(), "customer not equals");

        assertNotEquals(
                Customer.builder().id("1").name("John").build(),
                Customer.builder().id("2").name("John").build(), "customer not equals");
    }

}