package com.utcn.ds.predictionmanagement.controller.impl;


import com.utcn.ds.predictionmanagement.controller.WeatherController;
import com.utcn.ds.predictionmanagement.controller.dto.PredictedValueDTO;
import com.utcn.ds.predictionmanagement.controller.dto.TrainResultDTO;
import com.utcn.ds.predictionmanagement.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class WeatherControllerImpl implements WeatherController {

    private final WeatherService weatherService;

    @Override
    public ResponseEntity<List<TrainResultDTO>> trainModels(HttpServletRequest request) {
        return new ResponseEntity<>(weatherService.trainModels(request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PredictedValueDTO>> predictOnPeriod(String start, String end, Long deviceNumber, HttpServletRequest request) {
        return new ResponseEntity<>(weatherService.predictOnPeriod(start, end, deviceNumber, request), HttpStatus.OK);
    }
}
