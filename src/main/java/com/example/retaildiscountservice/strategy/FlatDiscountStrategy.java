package com.example.retaildiscountservice.strategy;

import org.springframework.stereotype.Component;
import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;

/*
 * flat discount strategy
 */
@Component
public class FlatDiscountStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Customer customer) {
        return true;
    }

    @Override
    public double applyPercentage(Bill bill) {
        return (int) bill.getTotalAmount() / 100 * 5;
    }

    public boolean isPercentageDiscount() {
        return false;
    }
}
