package com.example.pm_web.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pm_web.entity.TransactionsWRTPurse;

public interface TransactionsWRTPurseRepository
        extends JpaRepository<TransactionsWRTPurse, Long> {

    List<TransactionsWRTPurse> findByTransactionDate(LocalDate transactionDate);

    List<TransactionsWRTPurse> findAllByOrderByTransactionDateDesc();

    TransactionsWRTPurse findTopByOrderByTransactionIdDesc();
}
