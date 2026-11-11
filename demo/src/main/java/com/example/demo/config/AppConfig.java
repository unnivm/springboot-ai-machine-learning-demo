package com.example.demo.config;

import com.example.demo.ml.NaiveBayesClassifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public NaiveBayesClassifier naiveBayesClassifier(){
        return new NaiveBayesClassifier();
    }

}
