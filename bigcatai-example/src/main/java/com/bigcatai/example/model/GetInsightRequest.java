package com.bigcatai.example.model;

import java.util.List;

public class GetInsightRequest {

    private String question;
    private List<String> bankNames;
    private String startDate;
    private String endDate;
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public List<String> getBankNames() {
        return bankNames;
    }
    public void setBankNames(List<String> bankNames) {
        this.bankNames = bankNames;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
}
