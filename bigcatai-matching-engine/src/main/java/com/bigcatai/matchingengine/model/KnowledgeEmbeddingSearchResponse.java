package com.bigcatai.matchingengine.model;

import java.util.List;

public class KnowledgeEmbeddingSearchResponse {

    private List<KnowledgeEmbeddingSearchResult> knowledgeEmbeddings;

    public List<KnowledgeEmbeddingSearchResult> getKnowledgeEmbeddings() {
        return knowledgeEmbeddings;
    }

    public void setKnowledgeEmbeddings(List<KnowledgeEmbeddingSearchResult> knowledgeEmbeddings) {
        this.knowledgeEmbeddings = knowledgeEmbeddings;
    }
}
