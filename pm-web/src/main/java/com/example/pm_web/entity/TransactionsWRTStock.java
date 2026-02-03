package com.example.pm_web.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactionswrtstock")
public class TransactionsWRTStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long TransactionId;

    @Column(name = "dateOfTransaction")
    private LocalDate DateOfTransaction;

    @Column(name = "stock")
    private String Stock;

    @Column(name = "value")
    private Double Value;

    public Long getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.TransactionId = transactionId;
    }

    public LocalDate getDateOfTransaction() {
        return DateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.DateOfTransaction = dateOfTransaction;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        this.Stock = stock;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        this.Value = value;
    }
}
