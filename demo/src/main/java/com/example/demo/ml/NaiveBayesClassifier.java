package com.example.demo.ml;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class NaiveBayesClassifier {

    // Maps a Class Label to its total document count
    private final Map<String, Integer> classDocCounts = new HashMap<>();
    // Maps a Class Label to a nested Map of Word -> Word Count in that class
    private final Map<String, Map<String, Integer>> classWordCounts = new HashMap<>();
    // Maps a Class Label to the total number of words in that class
    private final Map<String, Integer> totalWordsPerClass = new HashMap<>();
    // Stores every unique word across all documents (the Vocabulary)
    private final Set<String> vocabulary = new HashSet<>();
    // Total documents used for training
    private int totalDocuments = 0;

    /**
     * Helper method to tokenize and clean strings into individual words.
     */
    private String[] tokenize(String text) {
        return text.toLowerCase().replaceAll("[^a-zA-Z0-String\\s]", "").split("\\s+");
    }

    /**
     * Trains the model using a document and its corresponding class label.
     */
    public void train(String text, String classLabel) {
        String[] words = tokenize(text);

        // Update document counts
        totalDocuments++;
        classDocCounts.put(classLabel, classDocCounts.getOrDefault(classLabel, 0) + 1);

        // Initialize nested maps if it's a new class label
        classWordCounts.putIfAbsent(classLabel, new HashMap<>());

        // Update word frequencies for this class
        Map<String, Integer> wordMap = classWordCounts.get(classLabel);
        for (String word : words) {
            if (word.isEmpty()) continue;

            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
            totalWordsPerClass.put(classLabel, totalWordsPerClass.getOrDefault(classLabel, 0) + 1);
            vocabulary.add(word);
        }
    }

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public List<String[]> trainFromFile() throws URISyntaxException, IOException {
        URL file = NaiveBayesClassifier.class.getClassLoader().getResource("Naive-Bayes-Classification-Data.csv");
        Path path = Paths.get(file.toURI());
        File file2 = path.toFile();
        BufferedReader br = new BufferedReader(new FileReader(file2));
        String line;
        List<String[]> data = new ArrayList<>();
        while((line = br.readLine())!=null) {
            String [] token = line.split(",");
            data.add(token);
        }
        return data;
    }

    /**
     *
     * @param text
     * @return
     */
    public PredictionResult classifyWithConfidence(String text) {
        String[] words = tokenize(text);
        int vocabSize = vocabulary.size();

        Map<String, Double> logScores = new HashMap<>();
        double maxLog = Double.NEGATIVE_INFINITY;

        // 1. find Log Score
        for (String classLabel : classDocCounts.keySet()) {
            double priorProb = (double) classDocCounts.get(classLabel) / totalDocuments;
            double logProbability = Math.log(priorProb);

            Map<String, Integer> wordMap = classWordCounts.get(classLabel);
            int totalWordsInThisClass = totalWordsPerClass.getOrDefault(classLabel, 0);

            for (String word : words) {
                if (word.isEmpty() || !vocabulary.contains(word)) continue;

                int wordCountInClass = wordMap.getOrDefault(word, 0);
                double wordConditionalProb = (double) (wordCountInClass + 1) / (totalWordsInThisClass + vocabSize);
                logProbability += Math.log(wordConditionalProb);
            }

            logScores.put(classLabel, logProbability);

            // find maximum Log Score
            if (logProbability > maxLog) {
                maxLog = logProbability;
            }
        }

        // 2. Use Softmax algorithm and convert Log Values to probability
        double totalExponentSum = 0.0;
        Map<String, Double> exponents = new HashMap<>();

        for (Map.Entry<String, Double> entry : logScores.entrySet()) {
            double expVal = Math.exp(entry.getValue() - maxLog);
            exponents.put(entry.getKey(), expVal);
            totalExponentSum += expVal;
        }

        String bestClass = null;
        double highestProb = 0.0;

        for (String classLabel : logScores.keySet()) {
            double finalProbability = exponents.get(classLabel) / totalExponentSum;
            if (finalProbability > highestProb) {
                highestProb = finalProbability;
                bestClass = classLabel;
            }
        }
        return new PredictionResult(bestClass, highestProb * 100);
    }
}