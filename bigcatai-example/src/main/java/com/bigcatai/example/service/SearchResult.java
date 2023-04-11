package com.bigcatai.example.service;

import com.bigcatai.example.vectordb.InsightSummaryDatabaseRow;

public class SearchResult {

    private double distance;
    private int dataId;
    private InsightSummaryDatabaseRow insightSummaryDatabaseRow;
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public InsightSummaryDatabaseRow getInsightSummaryDatabaseRow() {
        return insightSummaryDatabaseRow;
    }
    public void setInsightSummaryDatabaseRow(InsightSummaryDatabaseRow insightSummaryDatabaseRow) {
        this.insightSummaryDatabaseRow = insightSummaryDatabaseRow;
    }
    
}
