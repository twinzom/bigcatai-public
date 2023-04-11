package com.bigcatai.matchingengine.vectordatabase;

import java.io.IOException;
import java.util.List;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;

public interface VectorDatabaseInitializr {
    
    String index = "default-index";
    
    public void init() throws IOException;
}
