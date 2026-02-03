package com.example.pm_web.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions_wrt_purse", indexes = { @jakarta.persistence.Index(columnList = "transaction_date") })
public class TransactionsWRTPurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_value", nullable = false)
    private Double transactionValue;

    @Column(name = "purse_value_after", nullable = false)
    private Double purseValueAfter;

    public TransactionsWRTPurse() {
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

    public Double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Double getPurseValueAfter() {
        return purseValueAfter;
    }

    public void setPurseValueAfter(Double purseValueAfter) {
        this.purseValueAfter = purseValueAfter;
    }
}
