package com.example.pm_web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pm_web.entity.Holdings;

public interface holdings extends JpaRepository<Holdings, String> {

    @Query("""
        SELECT h.stock, SUM(h.totalInvestment)
        FROM Holdings h
        GROUP BY h.stock
    """)
    List<Object[]> sumByStockGrouped();

    @Query("SELECT SUM(h.totalInvestment) FROM Holdings h")
    Double sumAll();
}
