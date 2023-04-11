package com.bigcatai.example.vectordb;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bigcatai.example.service.CsvLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

public class DatabaseManager {

private static Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);
    
    private EmbeddingDatabase embeddingDatabase;
    private Map<Integer, InsightSummaryDatabaseRow> insightSummaryDatabase;

    public EmbeddingDatabase getEmbeddingDatabase() throws IOException {
        if (embeddingDatabase == null) {
            
            LOG.info("Init embedding database...");
            
            ObjectMapper objectMapper = new ObjectMapper();

            URL url = Resources.getResource("data/embedding-database.txt");
            String databaseText = Resources.toString(url, StandardCharsets.UTF_8);

            embeddingDatabase = objectMapper.readValue(databaseText, EmbeddingDatabase.class);
            
            LOG.info("Vector embedding database ready.");
        }
        return embeddingDatabase;
    }
    
    public Map<Integer, InsightSummaryDatabaseRow> getInsightSummaryDatabase() throws Exception {
        
        if (insightSummaryDatabase == null) {
            URL insightSummaryDatabaseTxtPath = Resources.getResource("data/insight-summary-database.txt");
            String insightSummaryDatabaseTxtContent= Resources.toString(insightSummaryDatabaseTxtPath, StandardCharsets.UTF_8);
            StringReader sr = new StringReader(insightSummaryDatabaseTxtContent);
            
            CsvLoader csvLoader = new CsvLoader();
            List<String[]> csvRows = csvLoader.readAll(sr);
            
            insightSummaryDatabase = new HashMap<>();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            for (String[] row: csvRows) {
                InsightSummaryDatabaseRow insightSummaryDatabaseRow = new InsightSummaryDatabaseRow();
                insightSummaryDatabaseRow.setUrl(row[1]);
                insightSummaryDatabaseRow.setBankName(row[2]);
                insightSummaryDatabaseRow.setTitle(row[3]);
                insightSummaryDatabaseRow.setSummary(row[4]);
                insightSummaryDatabaseRow.setPostDate(row[5]==null?null:dateFormat.parse(row[5]));
                insightSummaryDatabaseRow.setBriefSummary(row[6]);
                insightSummaryDatabase.put(Integer.parseInt(row[0]), insightSummaryDatabaseRow);
            }
        }
        return insightSummaryDatabase;
    }
    
}
