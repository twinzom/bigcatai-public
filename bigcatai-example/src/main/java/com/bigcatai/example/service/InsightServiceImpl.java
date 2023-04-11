package com.bigcatai.example.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigcatai.dialogflow.client.DialogflowDetectIntentClient;
import com.bigcatai.example.exception.WmgptException;
import com.bigcatai.example.model.GetInsightResponse;
import com.bigcatai.example.model.Insight;
import com.bigcatai.example.vectordb.DatabaseManager;
import com.bigcatai.example.vectordb.EmbeddingDatabaseRow;
import com.bigcatai.example.vectordb.InsightSummaryDatabaseRow;
import com.bigcatai.openai.client.OpenaiApiChatCompletionClient;
import com.bigcatai.openai.client.OpenaiApiCompletionClient;
import com.bigcatai.openai.client.OpenaiApiEmbeddingsClient;
import com.bigcatai.openai.client.models.OpenaiChatCompletionMessage;
import com.bigcatai.openai.client.models.OpenaiChatCompletionRequest;
import com.bigcatai.openai.client.models.OpenaiChatCompletionResponse;
import com.bigcatai.openai.client.models.OpenaiCompletionRequest;
import com.bigcatai.openai.client.models.OpenaiCompletionResponse;
import com.bigcatai.openai.client.models.OpenaiEmbeddingsRequest;
import com.bigcatai.openai.client.models.OpenaiEmbeddingsResponse;
import com.bigcatai.yahoofinance.client.YahooFinanceQuoteClient;
import com.bigcatai.yahoofinance.client.models.YahooFinanceQuoteResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;

@Service
public class InsightServiceImpl implements InsightService {

    private static Logger LOG = LoggerFactory.getLogger(InsightServiceImpl.class);
    
    
    @Autowired
    OpenaiApiEmbeddingsClient openaiApiEmbeddingsClient;

    @Autowired
    OpenaiApiCompletionClient openaiApiCompletionClient;
    
    @Autowired
    OpenaiApiChatCompletionClient openaiApiChatCompletionClient;
    
    @Autowired
    DialogflowDetectIntentClient dialogflowDetectIntentClient;
    
    @Autowired
    YahooFinanceQuoteClient yahooFinanceQuoteClient;
    
    private String PROMPT_SCRIPT = """ 
            Give INFORMATION provided below by the bank, please help bank staff to answer the QUESTION.
            1) The FINAL_ANSWER should base on the content in the JSON
            2) If you cannot find related information from INFORMATION, just say "Sorry, I don't have enough information". Don't mark up an answer.
            
            INFORMATION: 
            %s
            
            QUESTION: %s
            
            FINAL_ANWSER: 
            
            
            """;
    
    public static final String MODE = "OPEN_AI";
    
    List<EmbeddingDatabaseRow> embeddingDatabase;
    Map<Integer, InsightSummaryDatabaseRow> insightSummaryDatabase;
    
    public InsightServiceImpl () {
        
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            embeddingDatabase = databaseManager.getEmbeddingDatabase().getEmbeddingDatabaseRows();
            insightSummaryDatabase = databaseManager.getInsightSummaryDatabase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public GetInsightResponse askQuestion (String question,
            List<String> bankNames,
            Date startDate,
            Date endDate) throws WmgptException {

        Double[] questionEmbedding = embed(question);
        
        LOG.info("Find the most relate knowledge...");
        System.out.println("Find the most related knowledge... ");
        
        List<SearchResult> searchResults;
        try {
            searchResults = findMostRelatedAnswers(questionEmbedding, bankNames, startDate, endDate, 0.7, 3);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WmgptException("Error get most related answers.");
        }
        
        System.out.println("The result is: ");
        for (SearchResult searchResult : searchResults) {
            System.out.println("Distance = "+searchResult.getDistance());
            System.out.println("Title = "+searchResult.getInsightSummaryDatabaseRow().getTitle());
            System.out.println("Summary = "+searchResult.getInsightSummaryDatabaseRow().getSummary());
            System.out.println("-------------------------------------");
        }
        System.out.println("======================================");
        
        
        String answer;
        
        if (searchResults.size() >= 1) {
            answer =  getFinalAnswer(searchResults, question);
            //answer = getFinalAnswerChat(searchResults, question);
        } else {
            answer = "Sorry, I don't have enough information to give an answer.";
        }
        
        GetInsightResponse response = new GetInsightResponse();
        response.setOverallSummary(answer);
        response.setInsights(convertSearchResultsToInsights(searchResults));
        
        return response;
        
    }
    
    @Override
    public GetInsightResponse search (String question,
            List<String> bankNames,
            Date startDate,
            Date endDate) throws WmgptException {

        Double[] questionEmbedding = embed(question);
        
        LOG.info("Find the most relate knowledge...");
        System.out.println("Find the most related knowledge... ");
        
        List<SearchResult> searchResults;
        try {
            searchResults = findMostRelatedAnswers(questionEmbedding, bankNames, startDate, endDate, 0.7, 100);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WmgptException("Error get most related answers.");
        }
        
        GetInsightResponse response = new GetInsightResponse();
        response.setInsights(convertSearchResultsToInsights(searchResults));
        
        return response;
        
    }
    
    public Double[] embed(String question) throws WmgptException {
        LOG.info("Convert question into embedding...");
        OpenaiEmbeddingsRequest openaiEmbeddingsRequest = new OpenaiEmbeddingsRequest();
        openaiEmbeddingsRequest.setInput(question);
        openaiEmbeddingsRequest.setModel("text-embedding-ada-002");
        OpenaiEmbeddingsResponse openaiEmbeddingsResponse;
        try {
            openaiEmbeddingsResponse = openaiApiEmbeddingsClient.getEmbeddings(openaiEmbeddingsRequest);
            return openaiEmbeddingsResponse.getData()[0].getEmbedding();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WmgptException("Error when convert question text to embedding.");
        }
    }
    
    public String getFinalAnswer (List<SearchResult> searchResults, String question) throws WmgptException {
        
        LOG.info("getFinalAnswer");
        
        try {
            String information = convertSearchResultsToJson(searchResults);
        
            String prompt = String.format(PROMPT_SCRIPT, information, question);
            
            OpenaiCompletionRequest request = new OpenaiCompletionRequest();
            request.setTemperature(0.0);
            request.setPrompt(prompt);
            request.setModel("text-davinci-003");
            request.setMax_tokens(1900);
            
            OpenaiCompletionResponse openaiCompletionResponse;
            openaiCompletionResponse = openaiApiCompletionClient.getCompletion(request);
            //System.out.println ("Answer from OpenAI Davinci: " + openaiCompletionResponse.getChoices().get(0).getText());
            return openaiCompletionResponse.getChoices().get(0).getText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WmgptException("Error get final answer.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new WmgptException("Error get final answer.");
        }
        
    }
    
    public String getFinalAnswerChat (List<SearchResult> searchResults, String question) throws WmgptException {
        
        try {
            String information = convertSearchResultsToJson(searchResults);
            String prompt = String.format(PROMPT_SCRIPT, information, question);
            
            List<OpenaiChatCompletionMessage> messages = new ArrayList<OpenaiChatCompletionMessage>();
            messages.add(new OpenaiChatCompletionMessage("system", "You are a helpful assistant."));
            messages.add(new OpenaiChatCompletionMessage("user", prompt));
            
            OpenaiChatCompletionRequest request = new OpenaiChatCompletionRequest();
            request.setTemperature(0.0);
            request.setMessages(messages);
            request.setModel("gpt-4-0314");
            request.setMax_tokens(2000);
            
            OpenaiChatCompletionResponse openaiChatCompletionResponse;
            openaiChatCompletionResponse = openaiApiChatCompletionClient.getCompletion(request);
            //System.out.println ("Answer from OpenAI Davinci: " + openaiCompletionResponse.getChoices().get(0).getText());
            
            OpenaiChatCompletionMessage responseMessage = openaiChatCompletionResponse.getChoices().get(0).getMessage();
            
            return responseMessage.getContent();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WmgptException("Error get final answer.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new WmgptException("Error get final answer.");
        }
        
    }
    
    private String convertSearchResultsToJson (List<SearchResult> searchResults) throws JsonProcessingException {

        String information = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        for (SearchResult sr : searchResults) {
            
            information += "Article Title: "+sr.getInsightSummaryDatabaseRow().getTitle()+System.lineSeparator();
            information += "Article Date: "+dateFormat.format(sr.getInsightSummaryDatabaseRow().getPostDate())+System.lineSeparator();
            information += "Article Content: "+sr.getInsightSummaryDatabaseRow().getSummary();
            information += System.lineSeparator()+System.lineSeparator();
        }
        
        return information;
    }
    
    public List<SearchResult>  findMostRelatedAnswers (Double[] questionEmbedding, List<String> bankNames, Date startDate, Date endDate, double broad, int deep) throws SQLException {
        List<SearchResult> searchResults = new ArrayList<>(); 
        for(EmbeddingDatabaseRow row : embeddingDatabase) {
            SearchResult searchResult = new SearchResult();
            searchResult.setDistance(cosineSimilarity(row.getEmbedding(), questionEmbedding));
            searchResult.setDataId(row.getDataId());
            searchResult.setInsightSummaryDatabaseRow(insightSummaryDatabase.get(row.getDataId()));
            searchResults.add(searchResult);
        }
        
        searchResults = searchResults.stream()
                .sorted((sr1, sr2) -> Double.compare(sr2.getDistance(), sr1.getDistance()))
                .filter(sr -> sr.getDistance() > broad)
                .filter(sr -> bankNames.contains(sr.getInsightSummaryDatabaseRow().getBankName()))
                .filter(sr -> 
                    sr.getInsightSummaryDatabaseRow().getPostDate() != null
                        && sr.getInsightSummaryDatabaseRow().getPostDate().after(startDate)
                        && sr.getInsightSummaryDatabaseRow().getPostDate().before(endDate)
                )
                .limit(deep)
                .collect(Collectors.toList());
        
        
        return searchResults;
        
    }
    
    public double cosineSimilarity(Double[] doubles, Double[] inputTextEmbedding) {
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

    
    public class SearchResultJsonData {
        private int id;
        private String text;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }
    
    private List<Insight> convertSearchResultsToInsights (List<SearchResult> searchResults) {
        List<Insight> insights = new ArrayList<>();
        for (SearchResult searchResult : searchResults) {
            InsightSummaryDatabaseRow row = searchResult.getInsightSummaryDatabaseRow();
            Insight insight = new Insight();
            insight.setTitle(row.getTitle());
            insight.setUrl(row.getUrl());
            insight.setBreifSummary(row.getBriefSummary());
            insight.setBankName(row.getBankName());
            insight.setSummary(row.getSummary());
            insight.setRealation(searchResult.getDistance());
            insights.add(insight);
        }
        return insights;
    }

    @Override
    public GetInsightResponse askIntentQuestion(String question) throws WmgptException {
        try {
            DetectIntentResponse detectIntentResponse = dialogflowDetectIntentClient.detectIntent(question);
            System.out.println(detectIntentResponse);
            String intentName = detectIntentResponse.getQueryResult().getIntent().getDisplayName();
            if (intentName.equals("Default Fallback Intent")) {
                System.out.println("No intent found");
                return null;
            } else {
                GetInsightResponse response = new GetInsightResponse();
                System.out.println("Intent ["+intentName+"] found");
                List<YahooFinanceQuoteResponse> yahooFinanceQuoteResponses = processIntentFulfillment(detectIntentResponse);
                DecimalFormat df = new DecimalFormat("#.##");
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd MMM YYYY");
                if (yahooFinanceQuoteResponses != null) {
                    String summary = "";
                    for (YahooFinanceQuoteResponse yahooFinanceQuoteResponse : yahooFinanceQuoteResponses) {
                        if (yahooFinanceQuoteResponse.getMarketPrice() != null) {
                            summary += yahooFinanceQuoteResponse.getSymbol()
                            +" latest stock price is "+yahooFinanceQuoteResponse.getMarketPrice()
                            +", changed "+df.format(yahooFinanceQuoteResponse.getMarketChange())
                            +" ("+df.format(yahooFinanceQuoteResponse.getMarketChangePercent())+"%) \n";
                        }
                    }
                    response.setOverallSummary(summary);
                } else {
                    return null;
                }
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private List<YahooFinanceQuoteResponse> processIntentFulfillment (DetectIntentResponse detectIntentResponse) {
        Map<String, Value> fields = detectIntentResponse.getQueryResult().getParameters().getFieldsMap();
        Value value = fields.get("stock_ticker");
        if (value != null) {
            try {
                ListValue listValue = value.getListValue();
                if (listValue.getValuesCount() > 0) {
                    List<YahooFinanceQuoteResponse> responses = new ArrayList<>();
                    for (Value v : listValue.getValuesList()) {
                        String ticker = v.getStringValue();
                        responses.add(yahooFinanceQuoteClient.getQuote(ticker));
                    }
                    return responses;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    
}
