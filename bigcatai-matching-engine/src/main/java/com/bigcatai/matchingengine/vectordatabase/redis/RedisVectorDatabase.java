package com.bigcatai.matchingengine.vectordatabase.redis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;
import com.bigcatai.matchingengine.utils.BytesConvertor;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabase;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;

@Service
public class RedisVectorDatabase implements VectorDatabase {

    private static Logger LOG = LoggerFactory.getLogger(RedisVectorDatabaseInitializr.class);
    
    public List<KnowledgeEmbeddingSearchResult> searchSimilarVector (Double[] vector) {
        try(JedisPooled client = new JedisPooled("localhost", 6379)) {
            
            Query query = new Query("(@dt:["+getTimestampFourMonthsAgo()+" "+System.currentTimeMillis()+"])=>[KNN $K @v $BLOB AS relatedness_score]")
                    .addParam("K", 20)
                    .addParam("BLOB",BytesConvertor.doubleArrayToBytesLittleEndianOrder(vector))
                    .setSortBy("relatedness_score", true)
                    .returnFields("relatedness_score")
                    .dialect(2);
            
            List<Document> documents = client.ftSearch(index, query).getDocuments();
            
            LOG.info("Number of documents found: "+documents.size());
            
            List<KnowledgeEmbeddingSearchResult> result = new ArrayList<>();
            
            for (Document d : documents) {
                KnowledgeEmbeddingSearchResult knowledgeEmbedding = new KnowledgeEmbeddingSearchResult();
                knowledgeEmbedding.setKsid(Integer.parseInt(d.getId()));
                knowledgeEmbedding.setRelatedness(1-Double.valueOf(d.getString("relatedness_score")));
                result.add(knowledgeEmbedding);
            }
            
            return result;
        }
    }
    
    public static long getTimestampFourMonthsAgo() {
        LocalDateTime fourMonthsAgo = LocalDateTime.now().minus(4, ChronoUnit.MONTHS);
        return fourMonthsAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
}
