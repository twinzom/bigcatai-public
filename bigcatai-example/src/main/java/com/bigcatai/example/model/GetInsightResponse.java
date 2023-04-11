package com.bigcatai.example.model;

import java.util.List;

public class GetInsightResponse {

    private String overallSummary;
    private List<Insight> insights;
    private String error;
    
    public String getOverallSummary() {
        return overallSummary;
    }
    public void setOverallSummary(String overallSummary) {
        this.overallSummary = overallSummary;
    }
    public List<Insight> getInsights() {
        return insights;
    }
    public void setInsights(List<Insight> insights) {
        this.insights = insights;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    
}
