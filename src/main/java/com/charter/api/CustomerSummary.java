package com.charter.api;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data @Builder
public class CustomerSummary {
    Customer customer;

    int points30;
    int points60;
    int points90;
    int pointsOver90;
    int pointsTotal;

    public CustomerSummary mergePoints(CustomerSummary customerSummary) {
        this.points30 += customerSummary.getPoints30();
        this.points60 += customerSummary.getPoints60();
        this.points90 += customerSummary.getPoints90();
        this.pointsOver90 += customerSummary.getPointsOver90();
        this.pointsTotal += customerSummary.getPointsTotal();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerSummary)) return false;
        CustomerSummary that = (CustomerSummary) o;
        return Objects.equals(getCustomer(), that.getCustomer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer());
    }
}
