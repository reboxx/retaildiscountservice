package com.example.retaildiscountservice.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.example.retaildiscountservice.dto.BillRequest;

@Component
@Mapper(componentModel = "spring")
public interface BillMapper {

    Bill toBill(BillRequest request);
}