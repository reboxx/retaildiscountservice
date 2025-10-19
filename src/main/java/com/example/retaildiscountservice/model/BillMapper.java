package com.example.retaildiscountservice.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.retaildiscountservice.dto.BillRequest;
import com.example.retaildiscountservice.dto.BillResponse;

@Component
@Mapper(componentModel = "spring")
public interface BillMapper {

	Bill toBill(BillRequest request);

	BillResponse toBillResponse(Bill bill);
}