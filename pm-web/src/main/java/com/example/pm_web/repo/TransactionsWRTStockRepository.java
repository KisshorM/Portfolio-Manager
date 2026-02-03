package com.example.pm_web.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pm_web.entity.TransactionsWRTStock;

public interface TransactionsWRTStockRepository
        extends JpaRepository<TransactionsWRTStock, Long> {

    List<TransactionsWRTStock> findByStockSymbol(String stockSymbol);

    List<TransactionsWRTStock> findByTransactionDate(LocalDate transactionDate);

    List<TransactionsWRTStock> findByStockSymbolOrderByTransactionDateDesc(
            String stockSymbol
    );

    @Query("""
        SELECT SUM(t.transactionValue)
        FROM TransactionsWRTStock t
        WHERE t.stockSymbol = :stockSymbol
    """)
    Double getTotalInvestedForStock(String stockSymbol);
}
