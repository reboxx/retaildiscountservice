package com.example.retaildiscountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.retaildiscountservice.model.Bill;


public interface BillRepository  extends JpaRepository<Bill, Long>{

}