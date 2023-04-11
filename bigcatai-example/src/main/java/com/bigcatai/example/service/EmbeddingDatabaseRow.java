package com.bigcatai.example.service;

public class EmbeddingDatabaseRow {

    private Double[] embedding;
    private int id;
    
    public Double[] getEmbedding() {
        return embedding;
    }
    public void setEmbedding(Double[] embedding) {
        this.embedding = embedding;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}