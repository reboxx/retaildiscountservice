package com.example.retaildiscountservice.service.strategy;


import java.util.List;

import org.springframework.stereotype.Component;

import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.model.Role;


@Component
public class AffiliateDiscountStrategy implements DiscountStrategy {

	@Override
	public boolean isApplicable(Customer customer) {
		return customer.getRole().name().equalsIgnoreCase(Role.AFFILIATE.name());
	}

	@Override
	public double applyPercentage(Bill bill) {
		return bill.getTotalNonGroceryAmount() * 0.10;
	}
	
	
    public boolean isPercentageDiscount() {
        return true;
    }

}
