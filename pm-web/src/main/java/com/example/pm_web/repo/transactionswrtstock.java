package com.example.pm_web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pm_web.entity.TransactionsWRTStock;
import java.util.List;
import java.time.LocalDate;


public interface transactionswrtstock extends JpaRepository<TransactionsWRTStock, Long> {
    List<TransactionsWRTStock> findByDateOfTransaction(LocalDate dateOfTransaction);
    List<TransactionsWRTStock> findByStock(String stock);
}
