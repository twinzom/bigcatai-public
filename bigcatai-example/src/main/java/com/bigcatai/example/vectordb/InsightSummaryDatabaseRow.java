package com.bigcatai.example.vectordb;

import java.util.Date;

public class InsightSummaryDatabaseRow {

    private String url;
    private String bankName;
    private String title;
    private String summary;
    private Date postDate;
    private String briefSummary;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public String getBriefSummary() {
        return briefSummary;
    }
    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }
}
