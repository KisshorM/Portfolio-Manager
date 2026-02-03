package com.example.pm_web.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pm_web.service.TradeService;

@RestController
@RequestMapping("/api/trade")
public class TradeApiController {

    private final TradeService tradeService;

    public TradeApiController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/buy")
    public ResponseEntity<Map<String, Object>> buy(@RequestBody Map<String, Object> body) {
        String symbol = getString(body, "symbol");
        int quantity = getInt(body, "quantity");
        if (symbol == null || symbol.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing symbol"));
        }
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Quantity must be positive"));
        }
        String err = tradeService.executeBuy(symbol.toUpperCase(), quantity);
        if (err != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", err));
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Bought " + quantity + " share(s) of " + symbol));
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> sell(@RequestBody Map<String, Object> body) {
        String symbol = getString(body, "symbol");
        int quantity = getInt(body, "quantity");
        if (symbol == null || symbol.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing symbol"));
        }
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Quantity must be positive"));
        }
        String err = tradeService.executeSell(symbol.toUpperCase(), quantity);
        if (err != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", err));
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Sold " + quantity + " share(s) of " + symbol));
    }

    private static String getString(Map<String, Object> body, String key) {
        Object o = body.get(key);
        return o == null ? null : o.toString().trim();
    }

    private static int getInt(Map<String, Object> body, String key) {
        Object o = body.get(key);
        if (o == null) return 0;
        if (o instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
