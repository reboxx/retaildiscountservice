package com.example.retaildiscountservice.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.model.Role;

@Component
public class FlatDiscountStrategy implements DiscountStrategy {

	
	@Override
	public boolean isApplicable(Customer customer) {
		return true;
	}


	@Override
	public double applyPercentage(Bill bill) {
		return  ((int)bill.getTotalAmount()/100) * 5;
	}


    public boolean isPercentageDiscount() {
        return false;
    }
}
