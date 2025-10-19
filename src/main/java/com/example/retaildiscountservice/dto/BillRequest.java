package com.example.retaildiscountservice.dto;

import java.util.List;

import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.model.Item;

public class BillRequest {

	private Customer customer;
	private List<Item> items;

	public BillRequest() {
		// TODO Auto-generated constructor stub
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
