package com.example.pm_web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortfolioHistoryResponse {
    private List<String> labels;
    private List<Double> values;

    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public List<Double> getValues() { return values; }
    public void setValues(List<Double> values) { this.values = values; }
}
