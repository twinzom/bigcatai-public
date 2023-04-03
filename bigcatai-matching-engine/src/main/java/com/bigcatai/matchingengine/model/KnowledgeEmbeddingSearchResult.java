package com.bigcatai.matchingengine.model;

public class KnowledgeEmbeddingSearchResult {

    private Integer ksid;
    
    private Double relatedness;

    public Integer getKsid() {
        return ksid;
    }

    public void setKsid(Integer ksid) {
        this.ksid = ksid;
    }

    public Double getRelatedness() {
        return relatedness;
    }

    public void setRelatedness(Double relatedness) {
        this.relatedness = relatedness;
    }
    
}
