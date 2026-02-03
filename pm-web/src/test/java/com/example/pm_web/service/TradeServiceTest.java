package com.example.pm_web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pm_web.entity.Holdings;
import com.example.pm_web.entity.TransactionsWRTPurse;
import com.example.pm_web.repo.HoldingsRepository;
import com.example.pm_web.repo.TransactionsWRTPurseRepository;
import com.example.pm_web.repo.TransactionsWRTStockRepository;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private HoldingsRepository holdingsRepository;
    @Mock
    private TransactionsWRTPurseRepository purseRepository;
    @Mock
    private TransactionsWRTStockRepository stockTxnRepository;
    @Mock
    private StockPriceService stockPriceService;

    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        tradeService = new TradeService(holdingsRepository, purseRepository, stockTxnRepository, stockPriceService);
    }

    @Nested
    @DisplayName("executeBuy")
    class ExecuteBuy {

        @Test
        void rejectsInvalidQuantity() {
            assertThat(tradeService.executeBuy("AAPL", 0)).isNotNull();
            assertThat(tradeService.executeBuy("AAPL", -1)).isNotNull();
        }

        @Test
        void rejectsWhenPriceUnavailable() {
            when(stockPriceService.getLivePrice("AAPL")).thenReturn(null);
            assertThat(tradeService.executeBuy("AAPL", 5)).contains("Could not get current price");
        }

        @Test
        void rejectsInsufficientCash() {
            when(stockPriceService.getLivePrice("AAPL")).thenReturn(100.0);
            when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(null);
            String err = tradeService.executeBuy("AAPL", 10);
            assertThat(err).contains("Insufficient cash");
        }

        @Test
        void createsNewHoldingAndRecordsTransactions() {
            when(stockPriceService.getLivePrice("AAPL")).thenReturn(150.0);
            TransactionsWRTPurse lastPurse = new TransactionsWRTPurse();
            lastPurse.setPurseValueAfter(10_000.0);
            when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(lastPurse);
            when(holdingsRepository.findByStockSymbol("AAPL")).thenReturn(Optional.empty());

            assertThat(tradeService.executeBuy("AAPL", 2)).isNull();

            verify(holdingsRepository).save(any(Holdings.class));
            verify(purseRepository).save(any(TransactionsWRTPurse.class));
            verify(stockTxnRepository).save(any());
        }

        @Test
        void addsToExistingHolding() {
            when(stockPriceService.getLivePrice("AAPL")).thenReturn(150.0);
            TransactionsWRTPurse lastPurse = new TransactionsWRTPurse();
            lastPurse.setPurseValueAfter(10_000.0);
            when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(lastPurse);
            Holdings existing = new Holdings();
            existing.setStockSymbol("AAPL");
            existing.setQuantity(5);
            existing.setTotalInvestment(500.0);
            existing.setMarketValue(750.0);
            existing.setPl(250.0);
            when(holdingsRepository.findByStockSymbol("AAPL")).thenReturn(Optional.of(existing));

            assertThat(tradeService.executeBuy("AAPL", 3)).isNull();

            assertThat(existing.getQuantity()).isEqualTo(8);
            verify(holdingsRepository).save(existing);
        }
    }

    @Nested
    @DisplayName("executeSell")
    class ExecuteSell {

        @Test
        void rejectsInvalidQuantity() {
            assertThat(tradeService.executeSell("AAPL", 0)).isNotNull();
        }

        @Test
        void rejectsWhenNotHolding() {
            when(holdingsRepository.findByStockSymbol("AAPL")).thenReturn(Optional.empty());
            assertThat(tradeService.executeSell("AAPL", 5)).contains("don't own");
        }

        @Test
        void rejectsSellingMoreThanOwned() {
            Holdings h = new Holdings();
            h.setStockSymbol("AAPL");
            h.setQuantity(3);
            h.setTotalInvestment(300.0);
            when(holdingsRepository.findByStockSymbol("AAPL")).thenReturn(Optional.of(h));
            assertThat(tradeService.executeSell("AAPL", 5)).contains("only own 3");
        }

        @Test
        void sellsPartialAndUpdatesHolding() {
            Holdings h = new Holdings();
            h.setStockSymbol("AAPL");
            h.setQuantity(10);
            h.setTotalInvestment(1000.0);
            when(holdingsRepository.findByStockSymbol("AAPL")).thenReturn(Optional.of(h));
            when(stockPriceService.getLivePrice("AAPL")).thenReturn(120.0);
            TransactionsWRTPurse lastPurse = new TransactionsWRTPurse();
            lastPurse.setPurseValueAfter(500.0);
            when(purseRepository.findTopByOrderByTransactionIdDesc()).thenReturn(lastPurse);

            assertThat(tradeService.executeSell("AAPL", 4)).isNull();

            assertThat(h.getQuantity()).isEqualTo(6);
            verify(holdingsRepository).save(h);
            verify(purseRepository).save(any(TransactionsWRTPurse.class));
        }
    }
}
