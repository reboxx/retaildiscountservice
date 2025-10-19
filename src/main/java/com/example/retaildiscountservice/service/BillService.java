package com.example.retaildiscountservice.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.example.retaildiscountservice.dto.BillResponse;
import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.repository.BillRepository;
import com.example.retaildiscountservice.strategy.DiscountStrategy;

/*
 * bill service
 */
@Component
public class BillService {
    private final List<DiscountStrategy> discountStrategies;
    private final BillRepository billRepo;

    public BillService(List<DiscountStrategy> discountStrategies, BillRepository billRepo) {
        this.discountStrategies = discountStrategies;
        this.billRepo = billRepo;
    }

    /**
     * Calculates net payable amount for a bill after applying discounts.
     */
    public BillResponse calculatePayment(Bill bill) {

        Customer customer = bill.getCustomer();

        if (customer.isBlacklisted()) {
            throw new IllegalStateException("Customer is blacklisted and not eligible for billing.");
        }

        double percentageDiscount = 0;
        int flatDiscount = 0;

        for (DiscountStrategy strategy : discountStrategies) {

            if (strategy.isApplicable(customer) && strategy.isPercentageDiscount()) {
                percentageDiscount = strategy.applyPercentage(bill);
            } else if (strategy.isApplicable(customer)) {
                flatDiscount = (int) strategy.applyPercentage(bill);
            }
        }

        BillResponse response = new BillResponse();
        response.setTotalAmountBeforeDiscount(bill.getTotalAmount());
        response.setPercentageDiscount(percentageDiscount);
        response.setFlatDiscount(flatDiscount);
        response.setTotalAmountAfterDiscount(bill.getTotalAmount() - flatDiscount - percentageDiscount);

        billRepo.save(bill);
        return response;
    }

}