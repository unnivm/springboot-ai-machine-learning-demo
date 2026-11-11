package com.example.demo.controller;

import com.example.demo.model.PredictionRequest;
import com.example.demo.service.BloodPressureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BloodPressureController {
    private BloodPressureService demoService;
    public BloodPressureController(BloodPressureService demoService) {
            this.demoService = demoService;
    }

    @GetMapping("/status")
    public String getDiabeticStatus(@RequestParam("sugar") String sugarLevel) {
        System.out.println("sugar->" + sugarLevel);
        return  this.demoService.getSugarLevel(sugarLevel);
    }

    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictDiabeticStatus(@RequestBody PredictionRequest predictionRequest) {
        System.out.println("sugar->" + predictionRequest.getGlucose());
        String result = this.demoService.getSugarLevel(predictionRequest.getGlucose() + " " + predictionRequest.getPressure());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("email", predictionRequest.getEmail());
        response.put("riskLevel", result);
        response.put("message", "Analysis completed successfully for your health profile.");

        return ResponseEntity.ok(response);
    }
}
