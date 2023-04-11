package com.bigcatai.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VertexEmbeddingService {

    public static final String ACCESS_TOKEN = "ya29.a0AVvZVspCkn5hF0WWyRiFpz7XHblGfWdhur7aS6tyw9jkHFBRexdlZhiwaYGEeUQpZ0y_kV5KA8KCg5MzKgdYBBzRuGUOSmnuIDnIcmm35FkWwj0Jj6UA3feIK9qfJJtnCyb0XJKRKZBMGzyTyet25j_j-6fSB78YbaAWaCgYKAVESAQASFQGbdwaIhGKDv_oR70i-Xq-lDH8WoQ0171";
    public static final String URL = "https://us-central1-aiplatform.googleapis.com/v1/projects/wmgpt-376709/locations/us-central1/endpoints/1248750540037292032:rawPredict";
    
    public Double[] embedQuestion (String input) {
        
        VertexEmbedQuestionRequest vertexRequest = new VertexEmbedQuestionRequest(input);
        
        ObjectMapper objectMapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient();
        
        RequestBody requestBody;
        try {
            requestBody = RequestBody.create(
                    objectMapper.writeValueAsString(vertexRequest), 
                    MediaType.parse("application/json; charset=utf-8"));
            
            Request request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer "+ACCESS_TOKEN)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            
            String responseStr = response.body().string();
            
            VertexEmbeddingResponse vertexResponse = objectMapper.readValue(responseStr, VertexEmbeddingResponse.class);
            
            return vertexResponse.getPredictions().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
        
    }
    
    
    public class VertexEmbedQuestionRequest {
        private List<String> instances = new ArrayList<>();
        private String signature_name = "question_encoder";
        
        public VertexEmbedQuestionRequest(String question) {
            instances.add(question); 
        }
        
        public List<String> getInstances() {
            return instances;
        }
        public void setInstances(List<String> instances) {
            this.instances = instances;
        }
        public String getSignature_name() {
            return signature_name;
        }
        public void setSignature_name(String signature_name) {
            this.signature_name = signature_name;
        }
        
    }
    
    public class VertexEmbedAnswerRequest {
        private List<Map<String,String>> instances;
        private String signature_name = "response_encoder";
        public List<Map<String, String>> getInstances() {
            return instances;
        }
        public void setInstances(List<Map<String, String>> instances) {
            this.instances = instances;
        }
        public String getSignature_name() {
            return signature_name;
        }
        public void setSignature_name(String signature_name) {
            this.signature_name = signature_name;
        }
    }
    
}
