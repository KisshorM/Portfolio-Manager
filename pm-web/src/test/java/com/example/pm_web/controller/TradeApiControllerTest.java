package com.example.pm_web.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.pm_web.service.TradeService;

@WebMvcTest(TradeApiController.class)
class TradeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @DisplayName("POST /api/trade/buy returns 400 when symbol missing")
    void buyMissingSymbol() throws Exception {
        mockMvc.perform(post("/api/trade/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Missing symbol"));
    }

    @Test
    @DisplayName("POST /api/trade/buy returns 400 when quantity invalid")
    void buyInvalidQuantity() throws Exception {
        mockMvc.perform(post("/api/trade/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"symbol\": \"AAPL\", \"quantity\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/trade/buy returns 200 and success when trade succeeds")
    void buySuccess() throws Exception {
        when(tradeService.executeBuy(anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(post("/api/trade/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"symbol\": \"AAPL\", \"quantity\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bought 5 share(s) of AAPL"));
    }

    @Test
    @DisplayName("POST /api/trade/buy returns 400 when service returns error")
    void buyServiceError() throws Exception {
        when(tradeService.executeBuy(anyString(), anyInt())).thenReturn("Insufficient cash.");

        mockMvc.perform(post("/api/trade/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"symbol\": \"AAPL\", \"quantity\": 1000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Insufficient cash."));
    }

    @Test
    @DisplayName("POST /api/trade/sell returns 200 when sell succeeds")
    void sellSuccess() throws Exception {
        when(tradeService.executeSell(anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(post("/api/trade/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"symbol\": \"AAPL\", \"quantity\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
