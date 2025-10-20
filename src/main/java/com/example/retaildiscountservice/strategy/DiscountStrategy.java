package com.example.retaildiscountservice.strategy;

import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;

public interface DiscountStrategy {

    boolean isApplicable(Customer customer);

    double applyPercentage(Bill price);

//    boolean isPercentageDiscount();
}