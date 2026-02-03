package com.example.pm_web.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pm_web.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    Optional<Watchlist> findByStockSymbol(String stockSymbol);

    boolean existsByStockSymbol(String stockSymbol);

    void deleteByStockSymbol(String stockSymbol);
}
