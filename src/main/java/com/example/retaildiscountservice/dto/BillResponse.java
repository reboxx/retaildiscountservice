package com.example.retaildiscountservice.dto;

public class BillResponse {

	private double totalAmountBeforeDiscount; // Sum of item prices before discount
	private double percentageDiscount; // Highest percentage discount applied
	private double flatDiscount; // Flat discount applied ($5 per $100)
	private double totalAmountAfterDiscount; // Total after all discounts

	public BillResponse() {
	}

	public double getTotalAmount() {
		return totalAmountBeforeDiscount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmountBeforeDiscount = totalAmount;
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

	public double getNetPayable() {
		return totalAmountAfterDiscount;
	}

	public void setNetPayable(double netPayable) {
		this.totalAmountAfterDiscount = netPayable;
	}

}
