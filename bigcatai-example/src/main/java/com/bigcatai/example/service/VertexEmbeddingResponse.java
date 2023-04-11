package com.bigcatai.example.service;

import java.util.List;

public class VertexEmbeddingResponse {
    List<Double[]> predictions;

    public List<Double[]> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Double[]> predictions) {
        this.predictions = predictions;
    }
}
