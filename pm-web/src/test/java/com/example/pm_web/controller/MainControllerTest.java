package com.example.pm_web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.pm_web.service.HoldingsService;
import com.example.pm_web.service.PerformanceService;
import com.example.pm_web.service.PortfolioService;
import com.example.pm_web.service.PythonServiceCaller;

@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;
    @MockBean
    private PythonServiceCaller pythonServiceCaller;
    @MockBean
    private PerformanceService performanceService;
    @MockBean
    private HoldingsService holdingsService;

    @Test
    @DisplayName("GET / returns dashboard view")
    void dashboard() throws Exception {
        when(portfolioService.buildDashboardData())
                .thenReturn(Map.of(
                        "holdings", java.util.List.of(),
                        "stocksValue", 0.0,
                        "totalPortfolioValue", 0.0,
                        "totalReturn", 0.0,
                        "cashBalance", 0.0,
                        "totalInvestment", 0.0,
                        "portfolioHistoryLabels", java.util.List.of(),
                        "portfolioHistoryValues", java.util.List.of()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @DisplayName("GET /performance returns performance view")
    void performance() throws Exception {
        when(performanceService.buildPerformanceData())
                .thenReturn(Map.of(
                        "performanceHoldings", java.util.List.of(),
                        "totalPl", 0.0,
                        "bestPerformerSymbol", "—",
                        "portfolioVolatility", "—",
                        "dividendYield", "—"));

        mockMvc.perform(get("/performance"))
                .andExpect(status().isOk())
                .andExpect(view().name("performance"));
    }

    @Test
    @DisplayName("GET /market returns marketLookup view with default symbol")
    void marketDefaultSymbol() throws Exception {
        when(pythonServiceCaller.get_news_endpoint(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(org.springframework.http.ResponseEntity.ok(java.util.List.of()));
        com.example.pm_web.entity.GetStockData stockData = new com.example.pm_web.entity.GetStockData();
        stockData.setCurrentPrice(100.0);
        stockData.setMarketCap(2_500_000_000_000L);
        stockData.setTotalRevenue(400_000_000_000L);
        stockData.setTotalEmployees(150000);
        stockData.setWebsite("https://example.com");
        stockData.setLongBusinessSummary("Summary");
        stockData.setLongname("Test Inc.");
        stockData.setSector("Technology");
        stockData.setIndustry("Software");
        stockData.setRevenueGrowth(0.05);
        stockData.setProfitMargins(0.25);
        stockData.setReturnOnEquity(0.15);
        stockData.setEarningsGrowth(0.10);
        when(pythonServiceCaller.get_stock_data_endpoint(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(org.springframework.http.ResponseEntity.ok(stockData));

        mockMvc.perform(get("/market"))
                .andExpect(status().isOk())
                .andExpect(view().name("marketLookup"));
    }
}
