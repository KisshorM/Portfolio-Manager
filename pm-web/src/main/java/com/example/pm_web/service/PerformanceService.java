package com.example.pm_web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.pm_web.dto.PerformanceHoldingView;
import com.example.pm_web.dto.QuoteChangeResponse;
import com.example.pm_web.entity.GetNews;
import com.example.pm_web.entity.Holdings;
import com.example.pm_web.repo.HoldingsRepository;

@Service
public class PerformanceService {

    private final HoldingsRepository holdingsRepository;
    private final StockPriceService stockPriceService;
    private final PythonServiceCaller pythonServiceCaller;

    public PerformanceService(HoldingsRepository holdingsRepository,
                              StockPriceService stockPriceService,
                              PythonServiceCaller pythonServiceCaller) {
        this.holdingsRepository = holdingsRepository;
        this.stockPriceService = stockPriceService;
        this.pythonServiceCaller = pythonServiceCaller;
    }

    public Map<String, Object> buildPerformanceData() {
        List<Holdings> holdings = holdingsRepository.findAll();
        List<PerformanceHoldingView> viewList = new ArrayList<>();

        double totalPl = 0.0;
        String bestPerformerSymbol = null;
        double bestPerformerPlPct = Double.NEGATIVE_INFINITY;

        for (Holdings h : holdings) {
            Double priceObj = stockPriceService.getLivePrice(h.getStockSymbol());
            double currentPrice = (priceObj != null ? priceObj : 0.0);
            if (currentPrice == 0.0 && h.getQuantity() > 0) {
                currentPrice = h.getTotalInvestment() / h.getQuantity();
            }
            double marketValue = currentPrice * h.getQuantity();
            double pl = marketValue - h.getTotalInvestment();
            totalPl += pl;

            PerformanceHoldingView v = new PerformanceHoldingView();
            v.setStockSymbol(h.getStockSymbol());
            v.setQuantity(h.getQuantity());
            v.setCurrentPrice(currentPrice);
            v.setCostBasis(h.getTotalInvestment());
            v.setMarketValue(marketValue);
            v.setPl(pl);

            double plPct = h.getTotalInvestment() > 0
                ? (pl / h.getTotalInvestment()) * 100
                : 0.0;
            if (plPct > bestPerformerPlPct) {
                bestPerformerPlPct = plPct;
                bestPerformerSymbol = h.getStockSymbol();
            }

            String companyName = h.getStockSymbol();
            try {
                var stockData = pythonServiceCaller.get_stock_data_endpoint(h.getStockSymbol()).getBody();
                if (stockData != null && stockData.getLongname() != null && !stockData.getLongname().isEmpty()) {
                    companyName = stockData.getLongname();
                }
            } catch (Exception ignored) { }
            v.setCompanyName(companyName);

            double change24h = 0.0;
            try {
                QuoteChangeResponse qc = pythonServiceCaller.getQuoteChange(h.getStockSymbol());
                if (qc != null) {
                    change24h = qc.getChangePercent();
                }
            } catch (Exception ignored) { }
            v.setChangePercent24h(change24h);

            String sentiment = "Neutral";
            try {
                var newsList = pythonServiceCaller.get_news_endpoint(h.getStockSymbol()).getBody();
                if (newsList != null && !newsList.isEmpty()) {
                    Map<String, Integer> counts = new HashMap<>();
                    counts.put("Bullish", 0);
                    counts.put("Bearish", 0);
                    counts.put("Neutral", 0);
                    for (GetNews n : newsList) {
                        String s = n.getSentiment();
                        if (s != null && !s.isEmpty()) {
                            String norm = s.trim().toLowerCase();
                            if (norm.startsWith("bull")) counts.put("Bullish", counts.get("Bullish") + 1);
                            else if (norm.startsWith("bear")) counts.put("Bearish", counts.get("Bearish") + 1);
                            else counts.put("Neutral", counts.get("Neutral") + 1);
                        }
                    }
                    int max = 0;
                    for (Map.Entry<String, Integer> e : counts.entrySet()) {
                        if (e.getValue() > max) {
                            max = e.getValue();
                            sentiment = e.getKey();
                        }
                    }
                }
            } catch (Exception ignored) { }
            v.setSentiment(sentiment);

            viewList.add(v);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("performanceHoldings", viewList);
        model.put("totalPl", totalPl);
        model.put("bestPerformerSymbol", bestPerformerSymbol != null ? bestPerformerSymbol : "—");
        model.put("portfolioVolatility", "—"); // placeholder
        model.put("dividendYield", "—");       // placeholder
        return model;
    }
}
