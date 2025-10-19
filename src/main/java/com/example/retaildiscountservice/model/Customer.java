package com.example.retaildiscountservice.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("join_date")
    private LocalDate joinDate;
    @JsonProperty("blacklisted")
    private boolean blacklisted;

    public Customer() {
    }

    public Customer(int id, String name, Role role, LocalDate joinDate, boolean blackListed) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.joinDate = joinDate;
        this.blacklisted = blackListed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

}
