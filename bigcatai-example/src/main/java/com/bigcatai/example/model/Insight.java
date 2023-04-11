package com.bigcatai.example.model;

public class Insight {

    private String title;
    private String url;
    private String breifSummary;
    private String bankName;
    private String summary;
    private Double realation;
    
    public Double getRealation() {
        return realation;
    }
    public void setRealation(Double realation) {
        this.realation = realation;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getBreifSummary() {
        return breifSummary;
    }
    public void setBreifSummary(String breifSummary) {
        this.breifSummary = breifSummary;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
}
