package com.bigcatai.matchingengine.vectordatabase;

import java.util.List;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;

public interface VectorDatabase {
    
    String index = "default-index";
    
    public List<KnowledgeEmbeddingSearchResult> searchSimilarVector (Double[] vector);
}
