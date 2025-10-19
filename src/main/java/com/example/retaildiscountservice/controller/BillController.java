package com.example.retaildiscountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BillController {


	
    @PostMapping("/calculate")
    public ResponseEntity<Object> calculateDiscount(@RequestBody Object billRequest) {

        return ResponseEntity.ok(null);
    }
}
