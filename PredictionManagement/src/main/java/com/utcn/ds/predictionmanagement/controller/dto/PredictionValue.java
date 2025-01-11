package com.utcn.ds.predictionmanagement.controller.dto;

import java.util.List;
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
public class PredictionValue {

    private List<String> date;
    private List<String> dt;
    private List<String> rf;
    private List<String> mlp;
    private List<String> real;
    private List<String> rmse_dt;
    private List<String> mape_dt;
    private List<String> mse_dt;
    private List<String> rmse_rf;
    private List<String> mape_rf;
    private List<String> mse_rf;
    private List<String> rmse_mlp;
    private List<String> mape_mlp;
    private List<String> mse_mlp;
}

