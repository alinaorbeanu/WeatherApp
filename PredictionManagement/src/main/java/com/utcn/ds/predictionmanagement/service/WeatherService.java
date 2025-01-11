package com.utcn.ds.predictionmanagement.service;

import com.utcn.ds.predictionmanagement.controller.dto.PredictedValueDTO;
import com.utcn.ds.predictionmanagement.controller.dto.TrainResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeatherService {

    List<TrainResultDTO> trainModels(HttpServletRequest request);

    List<PredictedValueDTO> predictOnPeriod(String start, String end, Long deviceNumber, HttpServletRequest request);
}
