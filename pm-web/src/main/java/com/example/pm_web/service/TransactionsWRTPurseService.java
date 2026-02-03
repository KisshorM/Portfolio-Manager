package com.example.pm_web.service;

import org.springframework.stereotype.Service;

import com.example.pm_web.repo.TransactionsWRTPurseRepository;

@Service
public class TransactionsWRTPurseService {

    private final TransactionsWRTPurseRepository repo;

    public TransactionsWRTPurseService(TransactionsWRTPurseRepository repo) {
        this.repo = repo;
    }

    public double getCashBalance() {
        var txn = repo.findTopByOrderByTransactionIdDesc();
        return txn != null ? txn.getPurseValueAfter() : 0.0;
    }
}
