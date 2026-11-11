package com.example.demo.service;

import com.example.demo.ml.NaiveBayesClassifier;
import com.example.demo.ml.PredictionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.demo.BloodPressureApplication.log;

@Service
public class BloodPressureService {

    @Value("${sugar.no}")  String  noSugar;
    @Value("${sugar.yes}") String yesSugar;

    private NaiveBayesClassifier naiveBayesClassifier;
    public BloodPressureService(NaiveBayesClassifier naiveBayesClassifier) {
        this.naiveBayesClassifier = naiveBayesClassifier;
    }

    public String getSugarLevel(String sugarLevel) {
        String result = naiveBayesClassifier.classifyWithConfidence(sugarLevel).predictedClass;
        PredictionResult predictionResult = naiveBayesClassifier.classifyWithConfidence(sugarLevel);
        log.info("predicted class " + predictionResult.predictedClass + " " +predictionResult.confidencePercentage);
        if("0".equals(predictionResult.predictedClass)) {
            return noSugar;
        }
            return yesSugar;
    }
}
