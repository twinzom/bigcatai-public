package com.bigcatai.matchingengine.vectordatabase.memory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.utils.BytesConvertor;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabaseConfiguration;
import com.bigcatai.matchingengine.vectordatabase.VectorDatabaseInitializr;
import com.bigcatai.matchingengine.vectordatabase.VectorStore;
import com.bigcatai.matchingengine.vectordatabase.VectorStoreRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Schema;


@Service
@ConditionalOnProperty(
        value="bigcatai.vector-database.config.type", 
        havingValue = "MEMORY", 
        matchIfMissing = true)
public class MemoryVectorDatabaseInitializr implements VectorDatabaseInitializr {

    private static Logger LOG = LoggerFactory.getLogger(MemoryVectorDatabaseInitializr.class);
    
    @Autowired
    VectorDatabaseConfiguration config;
    
    @Autowired
    MemoryVectorDatabase memoryVectorDatabase;
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        this.loadVectors();
    }
    
    
    private void loadVectors () throws IOException {
        LOG.info("Load embedding to Memory now.");
        ObjectMapper objectMapper = new ObjectMapper();

        URL url = Resources.getResource("data/vector-store.txt");
        String databaseText = Resources.toString(url, StandardCharsets.UTF_8);
        VectorStore vectorStore = objectMapper.readValue(databaseText, VectorStore.class);
        
        memoryVectorDatabase.setVectorStore(vectorStore);
        
        LOG.info("Number of embeddings loaded: "+vectorStore.getVectorStoreRecords().size());
    }
    
}
