package com.bigcatai.matchingengine.vectordatabase.memory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;
import com.bigcatai.matchingengine.utils.BytesConvertor;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabase;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabaseConfiguration;
import com.bigcatai.matchingengine.vectordatabase.VectorStore;
import com.bigcatai.matchingengine.vectordatabase.VectorStoreRecord;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;

@Service
@ConditionalOnProperty(
        value="bigcatai.vector-database.config.type", 
        havingValue = "MEMORY", 
        matchIfMissing = true)
public class MemoryVectorDatabase implements VectorDatabase {

    private static Logger LOG = LoggerFactory.getLogger(MemoryVectorDatabase.class);
    
    @Autowired
    VectorDatabaseConfiguration config;
    
    VectorStore vectorStore;
    
    public List<KnowledgeEmbeddingSearchResult> searchSimilarVector (Double[] vector, double relatednessThreshold, int limitCount) {
        
        List<KnowledgeEmbeddingSearchResult> result = new ArrayList<>();
        
        for(VectorStoreRecord vsr : vectorStore.getVectorStoreRecords()) {
            
            double relatedness = cosineSimilarity(vsr.getEmbedding(), vector);
            
            if (relatedness >= relatednessThreshold
                    && vsr.getSourcePostDate().getTime() > getTimestampFourMonthsAgo()) {
                KnowledgeEmbeddingSearchResult knowledgeEmbedding = new KnowledgeEmbeddingSearchResult();
                knowledgeEmbedding.setKsid(vsr.getKsid());
                knowledgeEmbedding.setRelatedness(relatedness);
                result.add(knowledgeEmbedding);
            }
            
        }
        
        result = result.stream()
                .sorted((sr1, sr2) -> Double.compare(sr2.getRelatedness(), sr1.getRelatedness()))
                .limit(limitCount)
                .collect(Collectors.toList());
        
        LOG.info("Number of documents found after filter: "+result.size());
        
        return result;

    }
    
    private long getTimestampFourMonthsAgo() {
        LocalDateTime fourMonthsAgo = LocalDateTime.now().minus(4, ChronoUnit.MONTHS);
        return fourMonthsAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    private double cosineSimilarity(Double[] doubles, Double[] inputTextEmbedding) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < doubles.length; i++) {
            dotProduct += doubles[i] * inputTextEmbedding[i];
            normA += Math.pow(doubles[i], 2);
            normB += Math.pow(inputTextEmbedding[i], 2);
        }   
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    
    public void setVectorStore (VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    
}
