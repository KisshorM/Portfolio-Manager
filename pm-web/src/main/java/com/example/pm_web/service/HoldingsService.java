package com.example.pm_web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pm_web.entity.Holdings;
import com.example.pm_web.repo.HoldingsRepository;

@Service
public class HoldingsService {

    private final HoldingsRepository repo;

    public HoldingsService(HoldingsRepository repo) {
        this.repo = repo;
    }

    public List<Holdings> getAllHoldings() {
        return repo.findAll();
    }

    public double getTotalPortfolioValue() {
        Double value = repo.getTotalMarketValue();
        return value == null ? 0.0 : value;
    }

    public double getTotalPL() {
        Double pl = repo.getTotalPL();
        return pl == null ? 0.0 : pl;
    }
}
