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
@Table(name = "train_results")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainResult {


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
            name = "model",
            nullable = false
    )
    private String model;

    @Column(
            name = "MAPE",
            nullable = false
    )
    private Double MAPE;

    @Column(
            name = "MSE",
            nullable = false
    )
    private Double MSE;

    @Column(
            name = "RMSE",
            nullable = false
    )
    private Double RMSE;
}
