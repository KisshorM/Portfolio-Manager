package com.example.pm_web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.pm_web.config.ApiConstants;
import com.example.pm_web.entity.Holdings;

@Service
public class StockPriceService {

    private final RestTemplate restTemplate;

    public StockPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getLivePrices(List<Holdings> holdings) {
        Map<String, Double> prices = new HashMap<>();
        for (Holdings h : holdings) {
            Double price = getLivePrice(h.getStockSymbol());
            if (price != null) {
                prices.put(h.getStockSymbol(), price);
            }
        }
        return prices;
    }

    @Cacheable(value = "price", key = "#symbol.toUpperCase()", unless = "#result == null")
    public Double getLivePrice(String symbol) {
        try {
            return restTemplate.getForObject(
                    ApiConstants.PATH_PRICE_SINGLE + symbol,
                    Double.class);
        } catch (Exception e) {
            return null;
        }
    }
}
