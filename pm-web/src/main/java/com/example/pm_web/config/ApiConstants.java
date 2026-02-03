package com.example.pm_web.config;

/**
 * Centralized constants for external API paths (Python worker).
 * Base URL is configured via python.api.base-url.
 */
public final class ApiConstants {

    public static final String PATH_PRICE_SINGLE = "/price-single/";
    public static final String PATH_QUOTE_CHANGE = "/quote-change/";
    public static final String PATH_GET_NEWS = "/get-news/";
    public static final String PATH_GET_STOCK_DATA = "/get-stock-data/";
    public static final String PATH_PORTFOLIO_HISTORY = "/portfolio-history";

    private ApiConstants() {}
}
