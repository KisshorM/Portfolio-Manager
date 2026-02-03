package com.example.pm_web.dto;

public class PerformanceHoldingView {
    private String stockSymbol;
    private String companyName;
    private int quantity;
    private double currentPrice;
    private double costBasis;
    private double marketValue;
    private double pl;
    private double changePercent24h;
    private String sentiment; // Bullish, Bearish, Neutral

    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public double getCostBasis() { return costBasis; }
    public void setCostBasis(double costBasis) { this.costBasis = costBasis; }
    public double getMarketValue() { return marketValue; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }
    public double getPl() { return pl; }
    public void setPl(double pl) { this.pl = pl; }
    public double getChangePercent24h() { return changePercent24h; }
    public void setChangePercent24h(double changePercent24h) { this.changePercent24h = changePercent24h; }
    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
}
