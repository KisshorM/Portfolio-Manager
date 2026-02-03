package com.example.pm_web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "holdings", indexes = { @jakarta.persistence.Index(columnList = "stock_symbol", unique = true) })
public class Holdings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_symbol", nullable = false, unique = true)
    private String stockSymbol;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "total_investment", nullable = false)
    private double totalInvestment;

    @Column(name = "market_value", nullable = false)
    private double marketValue;

    @Column(nullable = false)
    private double pl;

    public Holdings() {}

    public Long getId() { return id; }
    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(double totalInvestment) { this.totalInvestment = totalInvestment; }

    public double getMarketValue() { return marketValue; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }

    public double getPl() { return pl; }
    public void setPl(double pl) { this.pl = pl; }
}
