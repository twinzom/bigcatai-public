package com.bigcatai.example.semanticsearch.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigcatai.example.exception.WmgptException;
import com.bigcatai.example.model.GetInsightRequest;
import com.bigcatai.example.model.GetInsightResponse;
import com.bigcatai.example.service.InsightService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/simpleQuery")
class InsightController {

    private static Logger LOG = LoggerFactory.getLogger(InsightController.class);
    
    @Autowired
    private InsightService simpleQueryService;
    
    @PostMapping("/insight")
    public GetInsightResponse getInsight (
            @RequestBody GetInsightRequest request
            ) throws JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(request));
        
        GetInsightResponse response = new GetInsightResponse();
        
        Date startDate;
        Date endDate;
        
        try {
        
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            
            if (request.getStartDate() == null)
                startDate = new Date(System.currentTimeMillis() - (30L * 24L * 60L * 60L * 1000L * 3));
            else 
                startDate = dateFormat.parse(request.getStartDate());
            
            
            if (request.getEndDate() == null)
                endDate = new Date();
            else 
                endDate = dateFormat.parse(request.getEndDate());
        
        } catch (Exception e) {
            response.setError("Bad request input");
            return response;
        }
        
        
        try {
            response = simpleQueryService.askQuestion(request.getQuestion(), request.getBankNames(), startDate, endDate);
        } catch (WmgptException e) {
            response.setError(e.getWmgptErrorMessage());
        }
        
        return response;
    } 
    
    @PostMapping("/question")
    public GetInsightResponse askQuestion (
            @RequestBody GetInsightRequest request
            ) throws JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(request));
        
        GetInsightResponse response = new GetInsightResponse();
        
        Date startDate;
        Date endDate;
        
        try {
        
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            
            if (request.getStartDate() == null)
                startDate = new Date(System.currentTimeMillis() - (30L * 24L * 60L * 60L * 1000L * 3));
            else 
                startDate = dateFormat.parse(request.getStartDate());
            
            
            if (request.getEndDate() == null)
                endDate = new Date();
            else 
                endDate = dateFormat.parse(request.getEndDate());
        
        } catch (Exception e) {
            response.setError("Bad request input");
            return response;
        }
        
        
        try {
            response = simpleQueryService.askIntentQuestion(request.getQuestion());
            if (response == null)
                response = simpleQueryService.askQuestion(request.getQuestion(), request.getBankNames(), startDate, endDate);
        } catch (WmgptException e) {
            response.setError(e.getWmgptErrorMessage());
        }
        
        return response;
    } 
    
    @PostMapping("/insight-search")
    public GetInsightResponse searchInsight (
            @RequestBody GetInsightRequest request
            ) throws JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(request));
        
        GetInsightResponse response = new GetInsightResponse();
        
        Date startDate;
        Date endDate;
        
        try {
        
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            
            if (request.getStartDate() == null)
                startDate = new Date(System.currentTimeMillis() - (30L * 24L * 60L * 60L * 1000L * 3));
            else 
                startDate = dateFormat.parse(request.getStartDate());
            
            
            if (request.getEndDate() == null)
                endDate = new Date();
            else 
                endDate = dateFormat.parse(request.getEndDate());
        
        } catch (Exception e) {
            response.setError("Bad request input");
            return response;
        }
        
        
        try {
            response = simpleQueryService.search(request.getQuestion(), request.getBankNames(), startDate, endDate);
        } catch (WmgptException e) {
            response.setError(e.getWmgptErrorMessage());
        }
        
        return response;
    } 
    
}
