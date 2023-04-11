package com.bigcatai.matchingengine.vectordatabase.redis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;
import com.bigcatai.matchingengine.utils.BytesConvertor;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabase;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabaseConfiguration;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;

@Service
@ConditionalOnProperty(
        value="bigcatai.vector-database.config.type", 
        havingValue = "REDIS", 
        matchIfMissing = false)
public class RedisVectorDatabase implements VectorDatabase {

    private static Logger LOG = LoggerFactory.getLogger(RedisVectorDatabase.class);
    
    @Autowired
    VectorDatabaseConfiguration config;
    
    public List<KnowledgeEmbeddingSearchResult> searchSimilarVector (Double[] vector, double relatednessThreshold, int limitCount) {
        
        try(JedisPooled client = new JedisPooled(config.getRedisHost(), Integer.parseInt(config.getRedisPort()))) {
            
            Query query = new Query("(@dt:["+getTimestampFourMonthsAgo()+" "+System.currentTimeMillis()+"])=>[KNN $K @v $BLOB AS relatedness_score]")
                    .addParam("K", limitCount)
                    .addParam("BLOB",BytesConvertor.doubleArrayToBytesLittleEndianOrder(vector))
                    .setSortBy("relatedness_score", true)
                    .returnFields("relatedness_score")
                    .dialect(2);
            
            List<Document> documents = client.ftSearch(index, query).getDocuments();
            
            LOG.info("Number of documents found before filter: "+documents.size());
            
            List<KnowledgeEmbeddingSearchResult> result = new ArrayList<>();
            
            for (Document d : documents) {
                
                double relatedness = 1-Double.valueOf(d.getString("relatedness_score"));
                if (relatedness >= relatednessThreshold) {
                    KnowledgeEmbeddingSearchResult knowledgeEmbedding = new KnowledgeEmbeddingSearchResult();
                    knowledgeEmbedding.setKsid(Integer.parseInt(d.getId()));
                    knowledgeEmbedding.setRelatedness(relatedness);
                    result.add(knowledgeEmbedding);
                }
            }
            
            LOG.info("Number of documents found after filter: "+result.size());
            
            return result;
        }
    }
    
    public static long getTimestampFourMonthsAgo() {
        LocalDateTime fourMonthsAgo = LocalDateTime.now().minus(4, ChronoUnit.MONTHS);
        return fourMonthsAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
}
