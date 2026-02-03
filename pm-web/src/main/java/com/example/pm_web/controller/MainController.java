package com.example.pm_web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pm_web.service.HoldingsService;
import com.example.pm_web.service.PerformanceService;
import com.example.pm_web.service.PortfolioService;
import com.example.pm_web.service.PythonServiceCaller;

@Controller
public class MainController {

    private final PortfolioService portfolioService;
    private final PythonServiceCaller pythonServiceCaller;
    private final HoldingsService holdingService;
    private final PerformanceService performanceService;

    public MainController(
            PortfolioService portfolioService,
            PythonServiceCaller pythonServiceCaller,
            HoldingsService holdingService,
            PerformanceService performanceService) {
        this.portfolioService = portfolioService;
        this.pythonServiceCaller = pythonServiceCaller;
        this.holdingService = holdingService;
        this.performanceService = performanceService;
    }

    // @GetMapping("/")
    // public String showDashboard(Model model) {

    //     Map<String, Object> dashboardData =
    //             portfolioService.buildDashboardData();

    //     model.addAllAttributes(dashboardData);
    //     model.addAttribute("activePage", "dashboard");

    //     return "dashboard";
    // }

    @GetMapping("/")
    public String showDashboard(Model model) {
        Map<String, Object> data = portfolioService.buildDashboardData();
        model.addAllAttributes(data);
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }


    @GetMapping("/performance")
    public String showPerformance(Model model) {
        Map<String, Object> data = performanceService.buildPerformanceData();
        model.addAllAttributes(data);
        model.addAttribute("activePage", "performance");
        return "performance";
    }

    @GetMapping("/market")
    public String showMarket(
            @RequestParam(name = "symbol", required = false, defaultValue = "AAPL") String symbol,
            Model model) {

        model.addAttribute("activePage", "market");
        model.addAttribute("symbol", symbol);

        model.addAttribute(
                "newsList",
                pythonServiceCaller.get_news_endpoint(symbol).getBody()
        );

        model.addAttribute(
                "stockData",
                pythonServiceCaller.get_stock_data_endpoint(symbol).getBody()
        );

        return "marketLookup";
    }
}
