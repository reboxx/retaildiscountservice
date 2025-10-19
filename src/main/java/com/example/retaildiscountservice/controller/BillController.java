package com.example.retaildiscountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.retaildiscountservice.dto.BillRequest;
import com.example.retaildiscountservice.dto.BillResponse;
import com.example.retaildiscountservice.model.BillMapper;
import com.example.retaildiscountservice.service.BillService;

@RestController
public class BillController {

	@Autowired
	BillService billService;

	@Autowired
	BillMapper billMapper;

	@PostMapping("/calculate")
	public ResponseEntity<Object> calculateDiscount(@RequestBody BillRequest billRequest) {

		BillResponse response = billService.calculatePayment(billMapper.toBill(billRequest));

		return ResponseEntity.ok(response);
	}
}
