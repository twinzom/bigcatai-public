package com.bigcatai.matchingengine.vectordatabase;

import java.util.List;

public class VectorStore {
    
    private String indexName;
    
    List<VectorStoreRecord> vectorStoreRecords;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<VectorStoreRecord> getVectorStoreRecords() {
        return vectorStoreRecords;
    }

    public void setVectorStoreRecords(List<VectorStoreRecord> vectorStoreRecords) {
        this.vectorStoreRecords = vectorStoreRecords;
    }

}
