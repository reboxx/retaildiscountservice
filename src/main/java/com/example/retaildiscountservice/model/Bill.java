package com.example.retaildiscountservice.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Bill {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Customer customer;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Item> items;

	public Bill() {
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

	public double getTotalNonGroceryAmount() {
		return items.stream().filter(a -> a.getCategory().name().equalsIgnoreCase(ItemCategory.NON_GROCERY.name()))
				.mapToDouble(Item::getPrice) // convert each Item to double
				.sum();
	}

	public double getTotalAmount() {
		return items.stream().mapToDouble(Item::getPrice) // convert each Item to double
				.sum();
	}
}