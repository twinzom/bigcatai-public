package com.bigcatai.matchingengine.service;

import java.util.List;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;

public interface KnowledgeEmbeddingSearchService {

    public List<KnowledgeEmbeddingSearchResult> search (
            String question, 
            double relatednessThreshold, 
            int limitCount);
    
}
