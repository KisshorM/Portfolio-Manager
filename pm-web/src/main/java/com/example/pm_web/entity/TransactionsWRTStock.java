package com.example.pm_web.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions_wrt_stock", indexes = { @jakarta.persistence.Index(columnList = "stock_symbol") })
public class TransactionsWRTStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;

    @Column(name = "transaction_value", nullable = false)
    private Double transactionValue;

    public TransactionsWRTStock() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Double transactionValue) {
        this.transactionValue = transactionValue;
    }
}
