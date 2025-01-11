package com.utcn.ds.predictionmanagement.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.utcn.ds.predictionmanagement.entity.WeatherData;
import com.utcn.ds.predictionmanagement.exception.SomethingWentWrong;
import com.utcn.ds.predictionmanagement.repository.WeatherRepository;
import com.utcn.ds.predictionmanagement.service.WeatherConsumerService;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherConsumerServiceImpl implements WeatherConsumerService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Override
    public void addWeatherDataToDB() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                WeatherData weatherData = new ObjectMapper().readValue(message, WeatherData.class);
                System.out.println(weatherData);

                this.weatherRepository.save(weatherData);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            throw new SomethingWentWrong("Weather Consumer failed to add data to DB!");
        }
        System.out.println("e in db");
    }
}
