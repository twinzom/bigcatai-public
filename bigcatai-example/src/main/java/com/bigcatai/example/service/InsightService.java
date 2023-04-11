package com.bigcatai.example.service;

import java.util.Date;
import java.util.List;

import com.bigcatai.example.exception.WmgptException;
import com.bigcatai.example.model.GetInsightResponse;

public interface InsightService {

    public GetInsightResponse askQuestion (
            String question,
            List<String> bankNames,
            Date startDate,
            Date endDate
            ) throws WmgptException;
    
    public GetInsightResponse askIntentQuestion (
            String question
            ) throws WmgptException;
    
    public GetInsightResponse search (
            String keywords,
            List<String> bankNames,
            Date startDate,
            Date endDate
            ) throws WmgptException;
}
