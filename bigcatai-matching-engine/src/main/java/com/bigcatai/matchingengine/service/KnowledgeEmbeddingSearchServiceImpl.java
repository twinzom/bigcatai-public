package com.bigcatai.matchingengine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigcatai.matchingengine.model.KnowledgeEmbeddingSearchResult;
import com.bigcatai.matchingengine.vectordatabase.redis.RedisVectorDatabase;
import com.bigcatai.openai.client.OpenaiApiEmbeddingsClient;
import com.bigcatai.openai.client.models.OpenaiEmbeddingsRequest;
import com.bigcatai.openai.client.models.OpenaiEmbeddingsResponse;

@Service
public class KnowledgeEmbeddingSearchServiceImpl implements KnowledgeEmbeddingSearchService {

    private static Logger LOG = LoggerFactory.getLogger(KnowledgeEmbeddingSearchServiceImpl.class);
    
    @Autowired
    RedisVectorDatabase redisVectorDatabase;
    
    @Autowired
    OpenaiApiEmbeddingsClient openaiApiEmbeddingsClient;
    
    @Override
    public List<KnowledgeEmbeddingSearchResult> search (String questionText, double relatednessThreshold, int limitCount) {
        Double[] questionVector;
        
        System.out.println(questionText);
        
        try {
            questionVector = this.embedQuestion(questionText);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        List<KnowledgeEmbeddingSearchResult> searchResult = redisVectorDatabase.searchSimilarVector(questionVector);
        return searchResult;
    }
    
    private Double[] embedQuestion (String questionText) throws IOException {
        OpenaiEmbeddingsRequest openaiEmbeddingsRequest = new OpenaiEmbeddingsRequest();
        openaiEmbeddingsRequest.setInput(questionText);
        openaiEmbeddingsRequest.setModel("text-embedding-ada-002");
        OpenaiEmbeddingsResponse openaiEmbeddingsResponse = openaiApiEmbeddingsClient.getEmbeddings(openaiEmbeddingsRequest);
        openaiEmbeddingsResponse = openaiApiEmbeddingsClient.getEmbeddings(openaiEmbeddingsRequest);
        return openaiEmbeddingsResponse.getData()[0].getEmbedding();
    }

}
