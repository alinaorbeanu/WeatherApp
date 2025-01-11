package com.utcn.ds.predictionmanagement.service.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.utcn.ds.predictionmanagement.entity.WeatherData;
import com.utcn.ds.predictionmanagement.exception.SomethingWentWrong;
import com.utcn.ds.predictionmanagement.service.WeatherConsumerService;
import com.utcn.ds.predictionmanagement.service.WeatherProducerService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherProducerServiceImpl implements WeatherProducerService {
    private final WeatherConsumerService weatherConsumerService;
    private List<WeatherData> weatherData;

    @Override
    public void getAndSendWeatherData() {
        int i = 0;
        this.getValues();
        while (i < weatherData.size()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String a = weatherData.get(i).toString();
                channel.basicPublish("", QUEUE_NAME, null, a.getBytes());
                System.out.println(" [" + i + "] Send '" + weatherData.get(i).toString() + "'");
                i++;
            } catch (Exception e) {
                throw new SomethingWentWrong("Weather Producer failed to get and send data!");
            }
        }
        this.weatherConsumerService.addWeatherDataToDB();
    }

    private void getValues() {
        weatherData = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.csv");
            Scanner sc = new Scanner(inputStream);
            sc.useDelimiter("\r\n");
            while (sc.hasNext()) {
                String[] parts = sc.next().split(",");
                weatherData.add(WeatherData.builder()
                        .date(parts[0])
                        .temperature(Double.valueOf(parts[1]))
                        .clearSky(Double.valueOf(parts[2]))
                        .coveredSky(Double.valueOf(parts[3]))
                        .cloudySky(Double.valueOf(parts[4]))
                        .cumulonimbusSky(Double.valueOf(parts[5]))
                        .hazeAtmosphere(Double.valueOf(parts[6]))
                        .fogAtmosphere(Double.valueOf(parts[7]))
                        .freezingFogAtmosphere(Double.valueOf(parts[8]))
                        .weakSnowfall(Double.valueOf(parts[9]))
                        .showerRain(Double.valueOf(parts[10]))
                        .weakRain(Double.valueOf(parts[11]))
                        .rain(Double.valueOf(parts[12]))
                        .waterAndGranulatedSnow(Double.valueOf(parts[13]))
                        .waterAndSnow(Double.valueOf(parts[14]))
                        .storm(Double.valueOf(parts[15]))
                        .flashAndLightning(Double.valueOf(parts[16]))
                        .stormWithRain(Double.valueOf(parts[17]))
                        .drizzle(Double.valueOf(parts[18]))
                        .snowShower(Double.valueOf(parts[19]))
                        .realValue(Double.valueOf(parts[20]))
                        .build());
            }
            inputStream.close();
            sc.close();
        } catch (IOException ignored) {
        }
    }
}