package com.bigcatai.matchingengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchRequest;
import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResponse;
import com.bigcatai.matchingengine.service.KnowledgeEmbeddingSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
@RequestMapping("/knowledgeEmbedding")
class KnowledgeEmbeddingController {

    private static Logger LOG = LoggerFactory.getLogger(KnowledgeEmbeddingController.class);
    
    @Autowired
    private KnowledgeEmbeddingSearchService knowledgeEmbeddingSearchService;
    
    @PostMapping
    public KnowledgeEmbeddingSearchResponse searchKnowledgeEmbeddings (
            @RequestBody KnowledgeEmbeddingSearchRequest request
            ) throws JsonProcessingException {
        
        KnowledgeEmbeddingSearchResponse response = new KnowledgeEmbeddingSearchResponse();
        
        response.setKnowledgeEmbeddings(knowledgeEmbeddingSearchService.search(
                request.getQuestion(), 
                0.8, 
                5));
        
        return response;
    } 
    
    @GetMapping
    public KnowledgeEmbeddingSearchResponse searchKnowledgeEmbeddings (
            @RequestParam String question
            ) throws JsonProcessingException {
        
        KnowledgeEmbeddingSearchResponse response = new KnowledgeEmbeddingSearchResponse();
        
        response.setKnowledgeEmbeddings(knowledgeEmbeddingSearchService.search(
                question, 
                0.8, 
                5));
        
        return response;
    } 
}
