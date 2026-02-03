package com.example.pm_web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Holdings {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private String stock;
    
    private int quantity;
    private double totalInvestment;
    
    public String getStock() {
        return stock;
    }
    public void setStock(String stock) {
        this.stock = stock;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTotalInvestment() {
        return totalInvestment;
    }
    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    
}
