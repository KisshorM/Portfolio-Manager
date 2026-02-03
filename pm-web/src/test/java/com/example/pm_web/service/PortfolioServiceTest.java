package com.example.pm_web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pm_web.dto.PortfolioHistoryResponse;
import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.TransactionsWRTPurse;
import com.example.pm_web.repo.HoldingsRepository;
import com.example.pm_web.repo.TransactionsWRTPurseRepository;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private HoldingsRepository holdingsRepository;
    @Mock
    private StockPriceService stockPriceService;
    @Mock
    private TransactionsWRTPurseRepository purseRepository;
    @Mock
    private PythonServiceCaller pythonServiceCaller;

    private PortfolioService portfolioService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        portfolioService = new PortfolioService(
                holdingsRepository, stockPriceService, purseRepository, pythonServiceCaller);
    }

    @Test
    @DisplayName("buildDashboardData returns empty totals when no holdings")
    void buildDashboardDataEmptyHoldings() {
        when(holdingsRepository.findAll()).thenReturn(List.of());
        TransactionsWRTPurse txn = new TransactionsWRTPurse();
        txn.setPurseValueAfter(1000.0);
        when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(txn);
        when(pythonServiceCaller.getPortfolioHistory(anyList(), anyDouble()))
                .thenReturn(new PortfolioHistoryResponse());

        var data = portfolioService.buildDashboardData();

        assertThat(data.get("holdings")).asList().isEmpty();
        assertThat(data.get("stocksValue")).isEqualTo(0.0);
        assertThat(data.get("cashBalance")).isEqualTo(1000.0);
        assertThat(data.get("totalPortfolioValue")).isEqualTo(1000.0);
        assertThat(data.get("totalReturn")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("buildDashboardData computes holdings and total return")
    void buildDashboardDataWithHoldings() {
        Holdings h = new Holdings();
        h.setStockSymbol("AAPL");
        h.setQuantity(10);
        h.setTotalInvestment(1000.0);
        when(holdingsRepository.findAll()).thenReturn(List.of(h));
        when(stockPriceService.getLivePrice("AAPL")).thenReturn(120.0);
        when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(null);
        PortfolioHistoryResponse history = new PortfolioHistoryResponse();
        history.setLabels(List.of("2025-01-01"));
        history.setValues(List.of(2200.0));
        when(pythonServiceCaller.getPortfolioHistory(anyList(), anyDouble())).thenReturn(history);

        var data = portfolioService.buildDashboardData();

        @SuppressWarnings("unchecked")
        List<?> holdings = (List<?>) data.get("holdings");
        assertThat(holdings).hasSize(1);
        assertThat(data.get("stocksValue")).isEqualTo(1200.0);
        assertThat(data.get("cashBalance")).isEqualTo(0.0);
        assertThat(data.get("totalReturn")).isEqualTo(20.0); // (1200-1000)/1000 * 100
        assertThat(data.get("portfolioHistoryLabels")).asList().containsExactly("2025-01-01");
        assertThat(data.get("portfolioHistoryValues")).asList().containsExactly(2200.0);
    }
}
