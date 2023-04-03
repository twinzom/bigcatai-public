package com.bigcatai.matchingengine.vectordatabase.redis;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.utils.BytesConvertor;
import com.bigcatai.matchingengine.vectordatabase.VectorStore;
import com.bigcatai.matchingengine.vectordatabase.VectorStoreRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Schema;


@Service
public class RedisVectorDatabaseInitializr {

    private static Logger LOG = LoggerFactory.getLogger(RedisVectorDatabaseInitializr.class);
    
    String index = "default-index";
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        try(JedisPooled client = new JedisPooled("localhost", 6379)) {
            client.ftDropIndex(index);
            this.createSchema(client);
            this.loadVectors(client);
        }
    }
    
    
    
    public void createSchema(JedisPooled client) {
        
        Map<String, Object> attr = new HashMap<>();
        attr.put("TYPE", "FLOAT64");
        attr.put("DIM", 1536);
        attr.put("DISTANCE_METRIC", "COSINE");
        
        Schema sc = new Schema()
                .addNumericField("dt")
                .addHNSWVectorField("v", attr);

        client.ftCreate(index, IndexOptions.defaultOptions(), sc);
    }
    
    public void loadVectors (JedisPooled client) throws IOException {
        LOG.info("Load embedding to Redis now.");
        ObjectMapper objectMapper = new ObjectMapper();

        URL url = Resources.getResource("data/vector-store.txt");
        String databaseText = Resources.toString(url, StandardCharsets.UTF_8);
        VectorStore vectorStore = objectMapper.readValue(databaseText, VectorStore.class);
        
        for (VectorStoreRecord r : vectorStore.getVectorStoreRecords()) {
            Map<byte[], byte[]> fields = new HashMap<>();
            fields.put("v".getBytes(), BytesConvertor.doubleArrayToBytesLittleEndianOrder(r.getEmbedding()));
            client.hset((""+r.getKsid()).getBytes(), fields);
            client.hsetnx((""+r.getKsid()), "dt", ""+r.getSourcePostDate().getTime());
        }
        
        LOG.info("Number of embeddings loaded: "+vectorStore.getVectorStoreRecords().size());
    }
    
}
