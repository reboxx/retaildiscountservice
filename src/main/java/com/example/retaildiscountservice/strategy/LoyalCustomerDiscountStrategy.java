package com.example.retaildiscountservice.strategy;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;
import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.model.Role;

/*
 * loyal customer discount strategy
 */
@Component
public class LoyalCustomerDiscountStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Customer customer) {
        return customer.getRole().name().equalsIgnoreCase(Role.LOYAL_CUSTOMER.name());

    }

    @Override
    public double applyPercentage(Bill bill) {
        if (Period.between(bill.getCustomer().getJoinDate(), LocalDate.now()).getYears() >= 2) {
            return bill.getTotalNonGroceryAmount() * 0.05;
        }
        return 0;
    }

    public boolean isPercentageDiscount() {
        return true;
    }

}
