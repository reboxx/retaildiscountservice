package com.example.retaildiscountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Bill Response Object
 */
public class BillResponse {

    @JsonProperty("total_amount_before_discount")
    private double totalAmountBeforeDiscount; // Sum of item prices before discount
    @JsonProperty("percentage_discount")
    private double percentageDiscount; // Highest percentage discount applied
    @JsonProperty("flat_discount")
    private double flatDiscount; // Flat discount applied ($5 per $100)
    @JsonProperty("total_amount_after_discount")
    private double totalAmountAfterDiscount; // Total after all discounts

    public BillResponse() {
    }

    public double getTotalAmountBeforeDiscount() {
        return totalAmountBeforeDiscount;
    }

    public void setTotalAmountBeforeDiscount(double totalAmountBeforeDiscount) {
        this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public double getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(double flatDiscount) {
        this.flatDiscount = flatDiscount;
    }

    public double getTotalAmountAfterDiscount() {
        return totalAmountAfterDiscount;
    }

    public void setTotalAmountAfterDiscount(double totalAmountAfterDiscount) {
        this.totalAmountAfterDiscount = totalAmountAfterDiscount;
    }

}
