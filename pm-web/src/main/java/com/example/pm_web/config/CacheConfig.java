package com.example.pm_web.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.price.ttl:90}")
    private int priceTtlSeconds;

    @Value("${cache.quote-change.ttl:120}")
    private int quoteChangeTtlSeconds;

    @Value("${cache.stock-data.ttl:300}")
    private int stockDataTtlSeconds;

    @Value("${cache.news.ttl:180}")
    private int newsTtlSeconds;

    @Bean
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCacheNames(java.util.List.of("price", "quoteChange", "stockData", "news"));
        manager.registerCustomCache("price", (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                Caffeine.newBuilder().expireAfterWrite(priceTtlSeconds, TimeUnit.SECONDS).maximumSize(500).build());
        manager.registerCustomCache("quoteChange", (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                Caffeine.newBuilder().expireAfterWrite(quoteChangeTtlSeconds, TimeUnit.SECONDS).maximumSize(500).build());
        manager.registerCustomCache("stockData", (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                Caffeine.newBuilder().expireAfterWrite(stockDataTtlSeconds, TimeUnit.SECONDS).maximumSize(200).build());
        manager.registerCustomCache("news", (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                Caffeine.newBuilder().expireAfterWrite(newsTtlSeconds, TimeUnit.SECONDS).maximumSize(200).build());
        return manager;
    }
}
