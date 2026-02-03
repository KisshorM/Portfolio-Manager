package com.example.pm_web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pm_web.entity.TransactionsWRTStock;
import com.example.pm_web.repo.TransactionsWRTStockRepository;

@Service
public class TransactionsWRTStockService {

    private final TransactionsWRTStockRepository repo;

    public TransactionsWRTStockService(TransactionsWRTStockRepository repo) {
        this.repo = repo;
    }

    public List<TransactionsWRTStock> getTransactionsForStock(String stock) {
        return repo.findByStockSymbol(stock);
    }
}
