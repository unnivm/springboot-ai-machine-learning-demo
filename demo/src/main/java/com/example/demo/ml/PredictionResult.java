package com.example.demo.ml;

public class PredictionResult {
    public final String predictedClass;
    public final double confidencePercentage;

    public PredictionResult(String predictedClass, double confidencePercentage) {
        this.predictedClass = predictedClass;
        this.confidencePercentage = confidencePercentage;
    }

    @Override
    public String toString() {
        return String.format("%s (Probability: %.2f%%)", predictedClass, confidencePercentage);
    }
}
