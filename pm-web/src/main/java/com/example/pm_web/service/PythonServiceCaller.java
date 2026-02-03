package com.example.pm_web.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.pm_web.config.ApiConstants;
import com.example.pm_web.dto.PortfolioHistoryResponse;
import com.example.pm_web.dto.QuoteChangeResponse;
import com.example.pm_web.entity.GetNews;
import com.example.pm_web.entity.GetStockData;
import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.WsPriceSym;
import com.example.pm_web.entity.WsStockSym;

@Service
public class PythonServiceCaller {

    private final RestTemplate restTemplate;

    public PythonServiceCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @org.springframework.cache.annotation.Cacheable(value = "news", key = "#symbol.toUpperCase()", unless = "#result == null || #result.body == null")
    public ResponseEntity<List<GetNews>> getNewsEndpoint(String symbol) {
        try {
            List<GetNews> list = restTemplate.exchange(
                    ApiConstants.PATH_GET_NEWS + symbol,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GetNews>>() {}).getBody();
            return ResponseEntity.ok(list != null ? list : List.of());
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @org.springframework.cache.annotation.Cacheable(value = "stockData", key = "#symbol.toUpperCase()", unless = "#result == null || #result.body == null")
    public ResponseEntity<GetStockData> getStockDataEndpoint(String symbol) {
        try {
            GetStockData data = restTemplate.getForObject(
                    ApiConstants.PATH_GET_STOCK_DATA + symbol,
                    GetStockData.class);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<WsStockSym> getStockLiveHistoricalDataEndpoint(String symbol) {
        try {
            WsStockSym sym = restTemplate.getForObject(
                    "http://localhost:8000/get-stock-live-historical-data/" + symbol,
                    WsStockSym.class);
            return ResponseEntity.ok(sym);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @org.springframework.cache.annotation.Cacheable(value = "quoteChange", key = "#symbol.toUpperCase()")
    public QuoteChangeResponse getQuoteChange(String symbol) {
        try {
            QuoteChangeResponse resp = restTemplate.getForObject(
                    ApiConstants.PATH_QUOTE_CHANGE + symbol,
                    QuoteChangeResponse.class);
            return resp != null ? resp : new QuoteChangeResponse();
        } catch (Exception e) {
            QuoteChangeResponse fallback = new QuoteChangeResponse();
            fallback.setChangePercent(0.0);
            fallback.setPreviousClose(0.0);
            return fallback;
        }
    }

    public PortfolioHistoryResponse getPortfolioHistory(List<Holdings> holdings, double cash) {
        try {
            java.util.Map<String, Object> body = new java.util.HashMap<>();
            java.util.List<java.util.Map<String, Object>> items = new java.util.ArrayList<>();
            for (Holdings h : holdings) {
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("symbol", h.getStockSymbol());
                item.put("quantity", h.getQuantity());
                items.add(item);
            }
            body.put("holdings", items);
            body.put("cash", cash);
            PortfolioHistoryResponse resp = restTemplate.postForObject(
                    ApiConstants.PATH_PORTFOLIO_HISTORY,
                    body,
                    PortfolioHistoryResponse.class);
            return resp != null ? resp : new PortfolioHistoryResponse();
        } catch (Exception e) {
            return new PortfolioHistoryResponse();
        }
    }

    /** @deprecated Use getNewsEndpoint */
    public ResponseEntity<List<GetNews>> get_news_endpoint(String symbol) {
        return getNewsEndpoint(symbol);
    }

    /** @deprecated Use getStockDataEndpoint */
    public ResponseEntity<GetStockData> get_stock_data_endpoint(String symbol) {
        return getStockDataEndpoint(symbol);
    }
}
