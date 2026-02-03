// package com.example.pm_web.service;

// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;

// import com.example.pm_web.entity.Holdings;
// import com.example.pm_web.entity.Watchlist;
// import com.example.pm_web.entity.TransactionsWRTStock;
// import com.example.pm_web.entity.TransactionsWRTPurse;
// import com.example.pm_web.repo.holdings;
// import com.example.pm_web.repo.watchlist;
// import com.example.pm_web.repo.transactionswrtstock;
// import com.example.pm_web.repo.transactionswrtpurse;

// @Service
// public class MainService {

//     private final holdings holdingsRepo;
//     private final watchlist watchlistRepo;
//     private final transactionswrtstock transactionsStockRepo;
//     private final transactionswrtpurse transactionsPurseRepo;

//     @Autowired
//     public MainService(
//             holdings holdingsRepo,
//             watchlist watchlistRepo,
//             transactionswrtstock transactionsStockRepo,
//             transactionswrtpurse transactionsPurseRepo) {

//         this.holdingsRepo = holdingsRepo;
//         this.watchlistRepo = watchlistRepo;
//         this.transactionsStockRepo = transactionsStockRepo;
//         this.transactionsPurseRepo = transactionsPurseRepo;
//     }

//     // --- Dashboard & General Holdings ---
//     public List<Holdings> getAllHoldings() {
//         return holdingsRepo.findAll();
//     }

//     public Double getTotalInvestment() {
//         Double total = holdingsRepo.sumAll();
//         return total != null ? total : 0.0;
//     }

//     public Map<String, Double> getTotalInvestmentByStock() {
//         Map<String, Double> stockInvestmentMap = new HashMap<>();
//         for (Object[] row : holdingsRepo.sumByStockGrouped()) {
//             stockInvestmentMap.put(
//                 (String) row[0],
//                 row[1] != null ? (Double) row[1] : 0.0
//             );
//         }
//         return stockInvestmentMap;
//     }

//     // --- Purse & Transactions (Used in other pages) ---
//     public List<TransactionsWRTPurse> getAllTransactionsWRTPurse() {
//         return transactionsPurseRepo.findAll();
//     }

//     public List<TransactionsWRTStock> getAllTransactionsWRTStock(LocalDate date) {
//         return transactionsStockRepo.findByDateOfTransaction(date);
//     }

//     public List<TransactionsWRTStock> getTransactionsByStock(String stock) {
//         return transactionsStockRepo.findByStock(stock);
//     }

//     // --- Watchlist ---
//     public List<Watchlist> getAllWatchlistItems() {
//         return watchlistRepo.findAll();
//     }
// }

// package com.example.pm_web.service;

// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;

// import com.example.pm_web.entity.Holdings;
// import com.example.pm_web.entity.Watchlist;
// import com.example.pm_web.entity.TransactionsWRTStock;
// import com.example.pm_web.entity.TransactionsWRTPurse;
// import com.example.pm_web.repo.holdings;
// import com.example.pm_web.repo.watchlist;
// import com.example.pm_web.repo.transactionswrtstock;
// import com.example.pm_web.repo.transactionswrtpurse;

// @Service
// public class MainService {

//     private final holdings holdingsRepo;
//     private final watchlist watchlistRepo;
//     private final transactionswrtstock transactionsStockRepo;
//     private final transactionswrtpurse transactionsPurseRepo;

//     @Autowired
//     public MainService(
//             holdings holdingsRepo,
//             watchlist watchlistRepo,
//             transactionswrtstock transactionsStockRepo,
//             transactionswrtpurse transactionsPurseRepo) {

//         this.holdingsRepo = holdingsRepo;
//         this.watchlistRepo = watchlistRepo;
//         this.transactionsStockRepo = transactionsStockRepo;
//         this.transactionsPurseRepo = transactionsPurseRepo;
//     }

//     // --- Dashboard & Holdings ---
//     public List<Holdings> getAllHoldings() {
//         return holdingsRepo.findAll();
//     }

//     public Double getTotalInvestment() {
//         Double total = holdingsRepo.sumAll();
//         return total != null ? total : 0.0;
//     }

//     public Map<String, Double> getTotalInvestmentByStock() {
//         Map<String, Double> stockInvestmentMap = new HashMap<>();
//         for (Object[] row : holdingsRepo.sumByStockGrouped()) {
//             stockInvestmentMap.put(
//                 (String) row[0],
//                 row[1] != null ? (Double) row[1] : 0.0
//             );
//         }
//         return stockInvestmentMap;
//     }

//     // --- Preserved Functions for Other Pages ---
//     public List<TransactionsWRTPurse> getAllTransactionsWRTPurse() {
//         return transactionsPurseRepo.findAll();
//     }

//     public List<TransactionsWRTStock> getAllTransactionsWRTStock(LocalDate date) {
//         return transactionsStockRepo.findByDateOfTransaction(date);
//     }

//     public List<TransactionsWRTStock> getTransactionsByStock(String stock) {
//         return transactionsStockRepo.findByStock(stock);
//     }

//     public List<Watchlist> getAllWatchlistItems() {
//         return watchlistRepo.findAll();
//     }

//     public Double getCurrentPurseValue() {
//         Double purseValue = transactionsPurseRepo.getLatestPurseValue();
//         return purseValue != null ? purseValue : 0.0;
//     }

// }





package com.example.pm_web.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.Watchlist;
import com.example.pm_web.entity.TransactionsWRTStock;
import com.example.pm_web.entity.TransactionsWRTPurse;
import com.example.pm_web.repo.holdings;
import com.example.pm_web.repo.watchlist;
import com.example.pm_web.repo.transactionswrtstock;
import com.example.pm_web.repo.transactionswrtpurse;

@Service
public class MainService {

    private final holdings holdingsRepo;
    private final watchlist watchlistRepo;
    private final transactionswrtstock transactionsStockRepo;
    private final transactionswrtpurse transactionsPurseRepo;

    @Autowired
    public MainService(
            holdings holdingsRepo,
            watchlist watchlistRepo,
            transactionswrtstock transactionsStockRepo,
            transactionswrtpurse transactionsPurseRepo) {

        this.holdingsRepo = holdingsRepo;
        this.watchlistRepo = watchlistRepo;
        this.transactionsStockRepo = transactionsStockRepo;
        this.transactionsPurseRepo = transactionsPurseRepo;
    }

    public List<Holdings> getAllHoldings() {
        return holdingsRepo.findAll();
    }

    public Double getTotalInvestment() {
        Double total = holdingsRepo.sumAll();
        return total != null ? total : 0.0;
    }

    public Double getCurrentPurseValue() {
        // Assuming your repo has this custom query or findFirstByOrderByIdDesc
        Double purseValue = transactionsPurseRepo.getLatestPurseValue();
        return purseValue != null ? purseValue : 0.0;
    }

    // --- Preserved Functions ---
    public List<TransactionsWRTPurse> getAllTransactionsWRTPurse() {
        return transactionsPurseRepo.findAll();
    }

    public List<TransactionsWRTStock> getAllTransactionsWRTStock(LocalDate date) {
        return transactionsStockRepo.findByDateOfTransaction(date);
    }

    public List<TransactionsWRTStock> getTransactionsByStock(String stock) {
        return transactionsStockRepo.findByStock(stock);
    }

    public List<Watchlist> getAllWatchlistItems() {
        return watchlistRepo.findAll();
    }
}