package com.example.pm_web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.pm_web.dto.HoldingView;
import com.example.pm_web.dto.PortfolioHistoryResponse;
import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.TransactionsWRTPurse;
import com.example.pm_web.repo.HoldingsRepository;
import com.example.pm_web.repo.TransactionsWRTPurseRepository;

@Service
public class PortfolioService {

    private final HoldingsRepository holdingsRepository;
    private final StockPriceService stockPriceService;
    private final TransactionsWRTPurseRepository purseRepository;
    private final PythonServiceCaller pythonServiceCaller;

    public PortfolioService(HoldingsRepository holdingsRepository,
                        StockPriceService stockPriceService,
                        TransactionsWRTPurseRepository purseRepository,
                        PythonServiceCaller pythonServiceCaller) {
        this.holdingsRepository = holdingsRepository;
        this.stockPriceService = stockPriceService;
        this.purseRepository = purseRepository;
        this.pythonServiceCaller = pythonServiceCaller;
    }

    public Map<String, Object> buildDashboardData() {
        List<Holdings> holdings = holdingsRepository.findAll();
        List<HoldingView> viewList = new ArrayList<>();

        double totalStockValue = 0.0;
        double totalInvestment = 0.0;

        for (Holdings h : holdings) {
            Double priceObj = stockPriceService.getLivePrice(h.getStockSymbol());
            double currentPrice = (priceObj != null && priceObj > 0)
                ? priceObj
                : (h.getQuantity() > 0 ? h.getTotalInvestment() / h.getQuantity() : 0);
            double marketValue = currentPrice * h.getQuantity();
            double pl = marketValue - h.getTotalInvestment();

            HoldingView v = new HoldingView();
            v.setStockSymbol(h.getStockSymbol());
            v.setQuantity(h.getQuantity());
            v.setCurrentPrice(currentPrice);
            v.setCostBasis(h.getTotalInvestment());
            v.setMarketValue(marketValue);
            v.setPl(pl);

            totalStockValue += marketValue;
            totalInvestment += h.getTotalInvestment();

            viewList.add(v);
        }

        TransactionsWRTPurse lastTxn =
        purseRepository.findTopByOrderByTransactionIdDesc();

        double cashBalance = lastTxn != null ? lastTxn.getPurseValueAfter() : 0.0;

        Map<String, Object> model = new HashMap<>();
        model.put("holdings", viewList);
        model.put("stocksValue", totalStockValue);
        model.put("totalPortfolioValue", totalStockValue + cashBalance);
        double totalReturnPct = totalInvestment > 0
            ? ((totalStockValue - totalInvestment) / totalInvestment) * 100
            : 0.0;
        model.put("totalReturn", totalReturnPct);

        model.put("cashBalance", cashBalance);
        model.put("totalPortfolioValue", totalStockValue + cashBalance);
        model.put("totalInvestment", totalInvestment);

        try {
            PortfolioHistoryResponse historyResp = pythonServiceCaller.getPortfolioHistory(holdings, cashBalance);
            model.put("portfolioHistoryLabels", historyResp.getLabels() != null ? historyResp.getLabels() : new ArrayList<>());
            model.put("portfolioHistoryValues", historyResp.getValues() != null ? historyResp.getValues() : new ArrayList<>());
        } catch (Exception e) {
            model.put("portfolioHistoryLabels", new ArrayList<String>());
            model.put("portfolioHistoryValues", new ArrayList<Double>());
        }

        return model;
    }
}
