package com.example.demo.model;

public class PredictionRequest {
    private int glucose;
    private double pressure;
    private int age;
    private String email;

    // Default Constructor (Spring-ന് JSON മാപ്പ് ചെയ്യാൻ ഇത് നിർബന്ധമാണ്)
    public PredictionRequest() {}

    // Getters and Setters
    public int getGlucose() { return glucose; }
    public void setGlucose(int glucose) { this.glucose = glucose; }

    public double getPressure() { return pressure; }
    public void setBmi(double bmi) { this.pressure = bmi; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

