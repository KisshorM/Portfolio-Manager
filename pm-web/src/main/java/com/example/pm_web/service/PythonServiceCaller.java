package com.example.pm_web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.example.pm_web.entity.GetNews;
import com.example.pm_web.entity.GetStockData;
import com.example.pm_web.entity.WsPriceSym;
import com.example.pm_web.entity.WsPriceSyms;
import com.example.pm_web.entity.WsStockSym;

@Service
public class PythonServiceCaller {

    private final RestTemplate restTemplate;

    @Autowired
    public PythonServiceCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ResponseEntity<List<GetNews>> get_news_endpoint(String symbol){
        String url = "http://localhost:8000/get-news/" + symbol;

        List<GetNews> newsList = restTemplate.exchange(url,HttpMethod.GET, null, new ParameterizedTypeReference<List<GetNews>>(){}).getBody();
        return ResponseEntity.ok(newsList);
    }

    public ResponseEntity<GetStockData> get_stock_data_endpoint(@PathVariable String symbol){
        String url = "http://localhost:8000/get-stock-data/" + symbol;
        
        GetStockData stockData = restTemplate.exchange(url,HttpMethod.GET, null, new ParameterizedTypeReference<GetStockData>(){}).getBody();
        return ResponseEntity.ok(stockData);
    }

    public ResponseEntity<WsPriceSym> get_stock_live_price_endpoint(@PathVariable String symbol){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/get-stock-live-price/" + symbol;
        
        WsPriceSym priceSym = restTemplate.getForObject(url, WsPriceSym.class);
        return ResponseEntity.ok(priceSym);
    }

    public ResponseEntity<WsPriceSyms> get_stocks_live_price_endpoint(@PathVariable List<String> symbols){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/get-stocks-live-price/" + String.join(",", symbols);
        
        WsPriceSyms priceSyms = restTemplate.getForObject(url, WsPriceSyms.class);
        return ResponseEntity.ok(priceSyms);
    }

    public ResponseEntity<WsStockSym> get_stock_live_historical_data_endpoint(@PathVariable String symbol){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/get-stock-live-historical-data/" + symbol;
        
        WsStockSym stockSym = restTemplate.getForObject(url, WsStockSym.class);
        return ResponseEntity.ok(stockSym);
    }
}
