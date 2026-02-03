package com.example.pm_web.entity;

import java.util.List;
import java.util.Map;

public class WsStockSym {
    private List<Map<String, Object>> data; // List of records from hist_json
    private String graph; // Base64 image string

    // Getters and Setters
    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }
}