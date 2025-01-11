package com.utcn.ds.predictionmanagement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcn.ds.predictionmanagement.controller.dto.PredictedValueDTO;
import com.utcn.ds.predictionmanagement.controller.dto.PredictionValue;
import com.utcn.ds.predictionmanagement.controller.dto.TrainResultDTO;
import com.utcn.ds.predictionmanagement.entity.PredictedValue;
import com.utcn.ds.predictionmanagement.entity.TrainResult;
import com.utcn.ds.predictionmanagement.repository.PredictedValueRepository;
import com.utcn.ds.predictionmanagement.repository.TrainResultRepository;
import com.utcn.ds.predictionmanagement.repository.WeatherRepository;
import com.utcn.ds.predictionmanagement.service.WeatherProducerService;
import com.utcn.ds.predictionmanagement.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherProducerService weatherProducerService;
    private final TrainResultRepository trainResultRepository;
    private final PredictedValueRepository predictedValueRepository;
    private final WeatherRepository weatherRepository;
    private final SimpMessagingTemplate template;
    private final ModelMapper modelMapper;

    @Override
    public List<TrainResultDTO> trainModels(HttpServletRequest request) {
        List<TrainResultDTO> predictions = new ArrayList<>();
        if (weatherRepository.findAll().isEmpty()) {
            this.weatherProducerService.getAndSendWeatherData();
        }

        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(6000));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://python:5000")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather/train")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            List<Map<String, Double>> toReturn = new ObjectMapper().readValue(s, new TypeReference<>() {
            });

            AtomicInteger i = new AtomicInteger(0);
            toReturn.forEach(entry -> {
                TrainResultDTO trainResultDTO = TrainResultDTO.builder()
                        .RMSE(entry.get("RMSE"))
                        .MAPE(entry.get("MAPE"))
                        .MSE(entry.get("MSE"))
                        .build();
                TrainResult trainResult = TrainResult.builder()
                        .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy hh:mm")))
                        .model(this.getModelName(i.get()))
                        .MAPE(trainResultDTO.getMAPE())
                        .RMSE(trainResultDTO.getRMSE())
                        .MSE(trainResultDTO.getMSE())
                        .build();
                predictions.add(trainResultDTO);
                this.trainResultRepository.save(trainResult);
                i.incrementAndGet();
            });
            this.template.convertAndSend("/train-models", predictions);
            return predictions;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PredictedValueDTO> predictOnPeriod(String start, String end, Long deviceNumber, HttpServletRequest request) {
        List<PredictionValue> predictions = new ArrayList<>();
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://python:5000")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/predict/start/" + start + "/end/" + end + "/deviceNumber/" + deviceNumber)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            List<Map<String, List<String>>> toReturn = new ObjectMapper().readValue(s, new TypeReference<>() {
            });

            toReturn.forEach(entry -> {
                PredictionValue predictionValue = PredictionValue.builder()
                        .date(entry.get("date"))
                        .dt(entry.get("dt"))
                        .rf(entry.get("rf"))
                        .mlp(entry.get("mlp"))
                        .real(entry.get("real"))
                        .rmse_dt(entry.get("rmse_dt"))
                        .mape_dt(entry.get("mape_dt"))
                        .mse_dt(entry.get("mse_dt"))
                        .rmse_rf(entry.get("rmse_rf"))
                        .mape_rf(entry.get("mape_rf"))
                        .mse_rf(entry.get("mse_rf"))
                        .rmse_mlp(entry.get("rmse_mlp"))
                        .mape_mlp(entry.get("mape_mlp"))
                        .mse_mlp(entry.get("mse_mlp"))
                        .build();
                predictions.add(predictionValue);
            });

            return mergeResults(predictions, start, end);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<PredictedValueDTO> mergeResults(List<PredictionValue> result, String start, String end) {
        List<PredictedValueDTO> merged = new ArrayList<>();
        Double realMean = 0.0;
        Double dtMean = 0.0;
        Double rfMean = 0.0;
        Double mlpMean = 0.0;
        String currentPredictDate = "";

        for (PredictionValue predictionValue : result) {
            for (int i = 0; i < predictionValue.getReal().size() - 1; i++) {
                if (predictionValue.getDate().get(i).equals(predictionValue.getDate().get(i + 1))) {
                    realMean += Double.parseDouble(predictionValue.getReal().get(i));
                    dtMean += Double.parseDouble(predictionValue.getDt().get(i));
                    rfMean += Double.parseDouble(predictionValue.getRf().get(i));
                    mlpMean += Double.parseDouble(predictionValue.getMlp().get(i));
                } else {
                    currentPredictDate = start + " -- " + end;
                    PredictedValue predictedValue = PredictedValue.builder()
                            .date(currentPredictDate)
                            .dt(dtMean)
                            .rf(rfMean)
                            .mlp(mlpMean)
                            .real(realMean)
                            .RMSE_DT(Double.parseDouble(predictionValue.getRmse_dt().get(0)))
                            .MAPE_DT(Double.parseDouble(predictionValue.getMape_dt().get(0)))
                            .MSE_DT(Double.parseDouble(predictionValue.getMse_dt().get(0)))
                            .RMSE_RF(Double.parseDouble(predictionValue.getRmse_rf().get(0)))
                            .MAPE_RF(Double.parseDouble(predictionValue.getMape_rf().get(0)))
                            .MSE_RF(Double.parseDouble(predictionValue.getMse_rf().get(0)))
                            .RMSE_MLP(Double.parseDouble(predictionValue.getRmse_mlp().get(0)))
                            .MAPE_MLP(Double.parseDouble(predictionValue.getMape_mlp().get(0)))
                            .MSE_MLP(Double.parseDouble(predictionValue.getMse_mlp().get(0)))
                            .build();

                    this.predictedValueRepository.save(predictedValue);
                    PredictedValueDTO predictedValueDTO = mapToPredictedValueDTO(predictedValue);
                    predictedValueDTO.setDate(predictionValue.getDate().get(i));

                    merged.add(predictedValueDTO);
                    realMean = 0.0;
                    dtMean = 0.0;
                    rfMean = 0.0;
                    mlpMean = 0.0;
                }
            }
        }
        Collections.reverse(merged);
        return merged;
    }

    private String getModelName(int i) {
        return switch (i) {
            case 0 -> "Decision Tree";
            case 1 -> "Random Forest";
            case 2 -> "MLP";
            default -> "";
        };
    }

    private PredictedValueDTO mapToPredictedValueDTO(PredictedValue predictedValue) {
        return modelMapper.map(predictedValue, PredictedValueDTO.class);
    }
}
