package com.example.pm_web.entity;

import java.util.Map;

public class WsPriceSyms {
    private String type; // LIVE_PRICE_UPDATE
    private Map<String, Object> prices; // Key: Symbol, Value: Double or "Error"

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Object> prices) {
        this.prices = prices;
    }
}