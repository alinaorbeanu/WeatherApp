package com.utcn.ds.predictionmanagement.service;

public interface WeatherConsumerService {
    String QUEUE_NAME = "weather";

    void addWeatherDataToDB();
}
