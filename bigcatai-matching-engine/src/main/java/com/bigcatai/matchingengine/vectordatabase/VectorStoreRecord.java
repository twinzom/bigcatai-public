package com.bigcatai.matchingengine.vectordatabase;

import java.util.Date;

public class VectorStoreRecord {

    private Integer ksid;
    private Date sourcePostDate;
    private Double[] embedding;
    
    public int getKsid() {
        return ksid;
    }
    public void setKsid(Integer ksid) {
        this.ksid = ksid;
    }
    public Date getSourcePostDate() {
        return sourcePostDate;
    }
    public void setSourcePostDate(Date sourcePostDate) {
        this.sourcePostDate = sourcePostDate;
    }
    public Double[] getEmbedding() {
        return embedding;
    }
    public void setEmbedding(Double[] embedding) {
        this.embedding = embedding;
    }
    
}
