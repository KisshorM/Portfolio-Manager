// package com.example.pm_web.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.example.pm_web.service.PythonServiceCaller;
// import com.example.pm_web.service.MainService;
// import com.example.pm_web.entity.GetNews;
// import com.example.pm_web.entity.GetStockData;
// import com.example.pm_web.entity.Holdings;

// import java.util.*;
// import org.springframework.beans.factory.annotation.Autowired;

// @Controller
// public class MainController {

//     @Autowired
//     private PythonServiceCaller pythonServiceCaller;
    
//     @Autowired
//     private MainService mainService;

    // @GetMapping("/")
    // public String showDashboard(Model model) {
    //     List<Holdings> holdings = mainService.getAllHoldings();
        
    //     // Calculate Total Portfolio Value (Assuming simple logic for this example)
    //     double totalMarketValue = holdings.stream()
    //             .mapToDouble(h -> h.getMarketValue() != null ? h.getMarketValue() : 0.0)
    //             .sum();
        
    //     double totalCostBasis = holdings.stream()
    //             .mapToDouble(h -> (h.getCostBasis() != null ? h.getCostBasis() : 0.0) * h.getShares())
    //             .sum();

    //     double totalReturnPercent = totalCostBasis > 0 ? ((totalMarketValue - totalCostBasis) / totalCostBasis) * 100 : 0;

    //     model.addAttribute("activePage", "dashboard");
    //     model.addAttribute("holdings", holdings);
    //     model.addAttribute("totalValue", totalMarketValue);
    //     model.addAttribute("totalReturn", totalReturnPercent);
        
    //     return "Dashboard";
    // }

    //     @GetMapping("/")
    //     public String showDashboard(Model model) {
    //         List<Holdings> holdings = mainService.getAllHoldings();
    //         double totalValue = holdings.stream().mapToDouble(h -> h.getMarketValue()).sum();

    //         model.addAttribute("holdingsList", holdings);
    //         model.addAttribute("totalPortfolioValue", totalValue);
    //         model.addAttribute("totalReturn", 12.8); // Example static or calculated
    //         model.addAttribute("activePage", "dashboard");
    //         return "Dashboard";
    //     }

    //     @GetMapping("/performance")
    //     public String showPerformance(Model model) {
    //         model.addAttribute("activePage", "performance");
    //         return "Performance";
    //     }

    //     @GetMapping("/market")
    //     public String showMarket(@RequestParam(name = "symbol", required = false, defaultValue = "AAPL") String sym, Model model) {
    //         model.addAttribute("activePage", "market");
    //         model.addAttribute("symbol", sym);

    //         List<GetNews> newsList = pythonServiceCaller.get_news_endpoint(sym).getBody();
    //         GetStockData stockData = pythonServiceCaller.get_stock_data_endpoint(sym).getBody();

    //         model.addAttribute("newsList", newsList);
    //         model.addAttribute("stockData", stockData);
            
    //         return "MarketLookup";
    //     }

    //     @GetMapping("/dashboard/cash-balance")
    //     public Double getCashBalance() {
    //         return mainService.getCurrentPurseValue();
    //     }

    // }













package com.example.pm_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pm_web.service.PythonServiceCaller;
import com.example.pm_web.service.MainService;
import com.example.pm_web.entity.GetNews;
import com.example.pm_web.entity.GetStockData;
import com.example.pm_web.entity.Holdings;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MainController {

    @Autowired
    private PythonServiceCaller pythonServiceCaller;
    
    @Autowired
    private MainService mainService;

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

    // Rest of your methods (Market, Performance, etc.)
    @GetMapping("/market")
    public String showMarket(@RequestParam(name = "symbol", required = false, defaultValue = "AAPL") String sym, Model model) {
        model.addAttribute("activePage", "market");
        model.addAttribute("symbol", sym);
        model.addAttribute("newsList", pythonServiceCaller.get_news_endpoint(sym).getBody());
        model.addAttribute("stockData", pythonServiceCaller.get_stock_data_endpoint(sym).getBody());
        return "MarketLookup";
    }

    @GetMapping("/dashboard/cash-balance")
    @ResponseBody // Added this to return raw data instead of a view
    public Double getCashBalance() {
        return mainService.getCurrentPurseValue();
    }

    @GetMapping("/dashboard/pl-stocks")
    @ResponseBody
    public Map<String, Double> getProfitAndLoss(){
        List<Holdings> holdings = mainService.getAllHoldings();
        Map<String, Double> plMap = new HashMap<>();

        for(Holdings h : holdings){
            Double marketPrice = pythonServiceCaller.get_stock_data_endpoint(h.getStock());

            Double pl = h.getTotalInvestment() - (h.getQuantity() * marketPrice);
            plMap.put(h.getStock(), pl);
        }

        return plMap;
    }
}