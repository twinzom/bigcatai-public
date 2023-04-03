package com.bigcatai.matchingengine.model;

import java.util.List;

public class KnowledgeEmbeddingSearchRequest {

    private String question;
    
    private List<String> sourceCodes;
    
    private String startDate;
    
    private String endDate;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getSourceCodes() {
        return sourceCodes;
    }

    public void setSourceCodes(List<String> sourceCodes) {
        this.sourceCodes = sourceCodes;
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
