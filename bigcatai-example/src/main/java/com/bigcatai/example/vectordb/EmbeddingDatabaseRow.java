package com.bigcatai.example.vectordb;

public class EmbeddingDatabaseRow {

    private int dataId;
    private Double[] embedding;
    
    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public Double[] getEmbedding() {
        return embedding;
    }
    public void setEmbedding(Double[] embedding) {
        this.embedding = embedding;
    }
}
