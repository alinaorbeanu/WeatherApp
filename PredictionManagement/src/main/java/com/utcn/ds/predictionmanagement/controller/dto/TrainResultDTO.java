package com.utcn.ds.predictionmanagement.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainResultDTO {

    private Double MAPE;
    private Double MSE;
    private Double RMSE;
}
