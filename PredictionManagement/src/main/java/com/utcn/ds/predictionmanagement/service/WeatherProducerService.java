package com.utcn.ds.predictionmanagement.service;

public interface WeatherProducerService {
    String QUEUE_NAME = "weather";

    void getAndSendWeatherData();
}