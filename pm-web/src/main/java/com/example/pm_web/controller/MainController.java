package com.example.pm_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pm_web.service.PythonServiceCaller;

import com.example.pm_web.entity.GetNews;
import com.example.pm_web.entity.GetStockData;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MainController {

    @Autowired
    private PythonServiceCaller pythonServiceCaller;

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "Dashboard";
    }

    @GetMapping("/performance")
    public String showPerformance(Model model) {
        model.addAttribute("activePage", "performance");
        return "Performance";
    }

    // @GetMapping("/market")
    // public String showMarket(Model model) {
    //     model.addAttribute("activePage", "market");

    //     String sym = "AMZN";
    //     List<GetNews> newsList = pythonServiceCaller.get_news_endpoint(sym).getBody();
    //     GetStockData stockData = pythonServiceCaller.get_stock_data_endpoint(sym).getBody();

    //     model.addAttribute("newsList", newsList);
    //     model.addAttribute("stockData", stockData);
    //     return "MarketLookup";
    // }

    @GetMapping("/market")
    public String showMarket(@RequestParam(name = "symbol", required = false, defaultValue = "AAPL") String sym, Model model) {
        model.addAttribute("activePage", "market");
        model.addAttribute("symbol", sym); // Send the symbol back to the UI

        // Fetch data dynamically based on the 'sym' variable
        List<GetNews> newsList = pythonServiceCaller.get_news_endpoint(sym).getBody();
        GetStockData stockData = pythonServiceCaller.get_stock_data_endpoint(sym).getBody();

        model.addAttribute("newsList", newsList);
        model.addAttribute("stockData", stockData);
        
        return "MarketLookup";
    }
}
