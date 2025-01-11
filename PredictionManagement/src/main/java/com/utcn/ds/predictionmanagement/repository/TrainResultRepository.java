package com.utcn.ds.predictionmanagement.repository;

import com.utcn.ds.predictionmanagement.entity.TrainResult;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TrainResultRepository extends JpaRepository<TrainResult, Long> {
}
