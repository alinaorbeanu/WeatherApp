package com.utcn.ds.predictionmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "predicted_value")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictedValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "date",
            nullable = false
    )
    private String date;

    @Column(
            name = "dt",
            nullable = false
    )
    private Double dt;

    @Column(
            name = "rf",
            nullable = false
    )
    private Double rf;

    @Column(
            name = "mlp",
            nullable = false
    )
    private Double mlp;

    @Column(
            name = "real",
            nullable = false
    )
    private Double real;

    @Column(
            name = "rmse_dt",
            nullable = false
    )
    private Double RMSE_DT;

    @Column(
            name = "mape_dt",
            nullable = false
    )
    private Double MAPE_DT;

    @Column(
            name = "mse_dt",
            nullable = false
    )
    private Double MSE_DT;

    @Column(
            name = "rmse_rf",
            nullable = false
    )
    private Double RMSE_RF;

    @Column(
            name = "mape_rf",
            nullable = false
    )
    private Double MAPE_RF;

    @Column(
            name = "mse_rf",
            nullable = false
    )
    private Double MSE_RF;

    @Column(
            name = "rmse_mlp",
            nullable = false
    )
    private Double RMSE_MLP;

    @Column(
            name = "mape_mlp",
            nullable = false
    )
    private Double MAPE_MLP;

    @Column(
            name = "mse_mlp",
            nullable = false
    )
    private Double MSE_MLP;
}
