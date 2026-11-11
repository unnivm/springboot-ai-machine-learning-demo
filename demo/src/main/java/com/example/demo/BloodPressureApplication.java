package com.example.demo;

import com.example.demo.ml.NaiveBayesClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BloodPressureApplication implements CommandLineRunner {

	public static final Logger log = LoggerFactory.getLogger(BloodPressureApplication.class);
	@Autowired
	private NaiveBayesClassifier naiveBayesClassifier;

	public static void main(String[] args) {
		SpringApplication.run(BloodPressureApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		List<String[]> data = naiveBayesClassifier.trainFromFile();
		data.remove(0);                              // skip the header row
		data.removeIf(r -> r.length < 3);            // skip blank or broken lines
		Collections.shuffle(data, new Random(42));   // fixed seed, repeatable result

		int split = (int) (data.size() * 0.8);
		List<String[]> trainSet = data.subList(0, split);
		List<String[]> testSet  = data.subList(split, data.size());

		// 1. train on 80%
		for (String[] arr : trainSet) {
			naiveBayesClassifier.train(arr[0].trim() + " " + arr[1].trim(), arr[2].trim());
		}

		// 2. test on the other 20%
		int tp = 0, tn = 0, fp = 0, fn = 0;
		for (String[] arr : testSet) {
			String text = arr[0].trim() + " " + arr[1].trim();
			String actual = arr[2].trim();
			String predicted = naiveBayesClassifier.classifyWithConfidence(text).predictedClass;

			if (predicted.equals("1") && actual.equals("1")) tp++;
			else if (predicted.equals("0") && actual.equals("0")) tn++;
			else if (predicted.equals("1") && actual.equals("0")) fp++;
			else fn++;
		}

		int total = tp + tn + fp + fn;
		double accuracy  = (tp + tn) / (double) total;
		double recall    = tp / (double) (tp + fn);
		double precision = tp / (double) (tp + fp);


		log.info(String.format("Test records: %d%n" , total));
		log.info(String.format("Accuracy : %.1f%%%n", accuracy * 100));
		log.info(String.format("Recall   : %.1f%%%n", recall * 100));
		log.info(String.format("Precision: %.1f%%%n", precision * 100));
		log.info(String.format("TP=%d TN=%d FP=%d FN=%d%n", tp, tn, fp, fn));

					String testData = "70 220";
					log.info("Diabetes: \"" + testData + "\" -> Predicted: " + naiveBayesClassifier.classifyWithConfidence(testData));
					if("0".equals(naiveBayesClassifier.classifyWithConfidence(testData))) {
							log.info("Not diabetic");
					}else {
						log.info("Yes, better start doing exercise");
					}
		}
	}
