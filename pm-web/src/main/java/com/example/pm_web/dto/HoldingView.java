package com.example.pm_web.dto;

public class HoldingView {

    private String stockSymbol;
    private int quantity;
    private double currentPrice;
    private double costBasis;
    private double marketValue;
    private double pl;
    
    public String getStockSymbol() {
        return stockSymbol;
    }
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public double getCostBasis() {
        return costBasis;
    }
    public void setCostBasis(double costBasis) {
        this.costBasis = costBasis;
    }
    public double getMarketValue() {
        return marketValue;
    }
    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }
    public double getPl() {
        return pl;
    }
    public void setPl(double pl) {
        this.pl = pl;
    }

    
}
