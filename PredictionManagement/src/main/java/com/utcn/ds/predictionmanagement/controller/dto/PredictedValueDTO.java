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
public class PredictedValueDTO {

    private String date;
    private Double dt;
    private Double rf;
    private Double mlp;
    private Double real;
    private Double rmse_dt;
    private Double mape_dt;
    private Double mse_dt;
    private Double rmse_rf;
    private Double mape_rf;
    private Double mse_rf;
    private Double rmse_mlp;
    private Double mape_mlp;
    private Double mse_mlp;
}
