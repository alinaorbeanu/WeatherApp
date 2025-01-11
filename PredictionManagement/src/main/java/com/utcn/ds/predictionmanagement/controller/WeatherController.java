package com.utcn.ds.predictionmanagement.controller;

import com.utcn.ds.predictionmanagement.controller.dto.PredictedValueDTO;
import com.utcn.ds.predictionmanagement.controller.dto.TrainResultDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface WeatherController {

    @GetMapping(value = "/weather/train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Models was trained!"),
            @ApiResponse(responseCode = "505", description = "Python microservice failed!")
    })
    ResponseEntity<List<TrainResultDTO>> trainModels(HttpServletRequest request);

    @GetMapping(value = "/weather/predict/start/{start}/end/{end}/deviceNumber/{deviceNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction results was retrieved!"),
            @ApiResponse(responseCode = "505", description = "Python microservice failed!")
    })
    ResponseEntity<List<PredictedValueDTO>> predictOnPeriod(@PathVariable String start, @PathVariable String end, @PathVariable Long deviceNumber, HttpServletRequest request);

}
