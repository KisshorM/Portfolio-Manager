package com.example.pm_web.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.TransactionsWRTPurse;
import com.example.pm_web.entity.TransactionsWRTStock;
import com.example.pm_web.repo.HoldingsRepository;
import com.example.pm_web.repo.TransactionsWRTPurseRepository;
import com.example.pm_web.repo.TransactionsWRTStockRepository;

@Service
public class TradeService {

    private final HoldingsRepository holdingsRepository;
    private final TransactionsWRTPurseRepository purseRepository;
    private final TransactionsWRTStockRepository stockTxnRepository;
    private final StockPriceService stockPriceService;

    public TradeService(HoldingsRepository holdingsRepository,
                        TransactionsWRTPurseRepository purseRepository,
                        TransactionsWRTStockRepository stockTxnRepository,
                        StockPriceService stockPriceService) {
        this.holdingsRepository = holdingsRepository;
        this.purseRepository = purseRepository;
        this.stockTxnRepository = stockTxnRepository;
        this.stockPriceService = stockPriceService;
    }

    /** @return error message or null if success */
    @Transactional
    public String executeBuy(String symbol, int quantity) {
        if (quantity <= 0) return "Quantity must be positive.";
        Double priceObj = stockPriceService.getLivePrice(symbol);
        if (priceObj == null || priceObj <= 0) return "Could not get current price for " + symbol;
        double price = priceObj;
        double cost = quantity * price;

        TransactionsWRTPurse lastPurse = purseRepository.findTopByOrderByTransactionIdDesc();
        double cashNow = lastPurse != null ? lastPurse.getPurseValueAfter() : 0.0;
        if (cashNow < cost) return "Insufficient cash. Need $" + String.format("%.2f", cost) + ", have $" + String.format("%.2f", cashNow) + ".";

        Optional<Holdings> opt = holdingsRepository.findByStockSymbol(symbol);
        Holdings h;
        if (opt.isPresent()) {
            h = opt.get();
            h.setQuantity(h.getQuantity() + quantity);
            h.setTotalInvestment(h.getTotalInvestment() + cost);
        } else {
            h = new Holdings();
            h.setStockSymbol(symbol);
            h.setQuantity(quantity);
            h.setTotalInvestment(cost);
            h.setMarketValue(cost);
            h.setPl(0.0);
        }
        double newMarketVal = h.getQuantity() * price;
        h.setMarketValue(newMarketVal);
        h.setPl(newMarketVal - h.getTotalInvestment());
        holdingsRepository.save(h);

        TransactionsWRTPurse purseTxn = new TransactionsWRTPurse();
        purseTxn.setTransactionDate(LocalDate.now());
        purseTxn.setTransactionValue(-cost);
        purseTxn.setPurseValueAfter(cashNow - cost);
        purseRepository.save(purseTxn);

        TransactionsWRTStock stockTxn = new TransactionsWRTStock();
        stockTxn.setTransactionDate(LocalDate.now());
        stockTxn.setStockSymbol(symbol);
        stockTxn.setTransactionValue(cost);
        stockTxnRepository.save(stockTxn);

        return null;
    }

    /** @return error message or null if success */
    @Transactional
    public String executeSell(String symbol, int quantity) {
        if (quantity <= 0) return "Quantity must be positive.";
        Optional<Holdings> opt = holdingsRepository.findByStockSymbol(symbol);
        if (opt.isEmpty()) return "You don't own any " + symbol + ".";
        Holdings h = opt.get();
        if (h.getQuantity() < quantity) return "You only own " + h.getQuantity() + " shares. Cannot sell " + quantity + ".";

        Double priceObj = stockPriceService.getLivePrice(symbol);
        if (priceObj == null || priceObj <= 0) return "Could not get current price for " + symbol;
        double price = priceObj;
        double proceeds = quantity * price;

        double costPerShare = h.getTotalInvestment() / h.getQuantity();
        double costRemoved = quantity * costPerShare;
        h.setQuantity(h.getQuantity() - quantity);
        h.setTotalInvestment(h.getTotalInvestment() - costRemoved);
        if (h.getQuantity() <= 0) {
            holdingsRepository.delete(h);
        } else {
            double newMarketVal = h.getQuantity() * price;
            h.setMarketValue(newMarketVal);
            h.setPl(newMarketVal - h.getTotalInvestment());
            holdingsRepository.save(h);
        }

        TransactionsWRTPurse lastPurse = purseRepository.findTopByOrderByTransactionIdDesc();
        double cashNow = lastPurse != null ? lastPurse.getPurseValueAfter() : 0.0;
        TransactionsWRTPurse purseTxn = new TransactionsWRTPurse();
        purseTxn.setTransactionDate(LocalDate.now());
        purseTxn.setTransactionValue(proceeds);
        purseTxn.setPurseValueAfter(cashNow + proceeds);
        purseRepository.save(purseTxn);

        TransactionsWRTStock stockTxn = new TransactionsWRTStock();
        stockTxn.setTransactionDate(LocalDate.now());
        stockTxn.setStockSymbol(symbol);
        stockTxn.setTransactionValue(-proceeds);
        stockTxnRepository.save(stockTxn);

        return null;
    }
}
