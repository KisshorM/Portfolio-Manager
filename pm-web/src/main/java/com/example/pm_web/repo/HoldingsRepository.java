package com.example.pm_web.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pm_web.entity.Holdings;

public interface HoldingsRepository extends JpaRepository<Holdings, Long> {

    Optional<Holdings> findByStockSymbol(String stockSymbol);

    boolean existsByStockSymbol(String stockSymbol);

    void deleteByStockSymbol(String stockSymbol);

    @Query("SELECT SUM(h.totalInvestment) FROM Holdings h")
    Double getTotalPortfolioInvestment();

    @Query("SELECT SUM(h.quantity) FROM Holdings h")
    Long getTotalQuantityHeld();

    @Query("SELECT SUM(h.marketValue) FROM Holdings h")
    Double getTotalMarketValue();

    @Query("SELECT SUM(h.pl) FROM Holdings h")
    Double getTotalPL();
}
