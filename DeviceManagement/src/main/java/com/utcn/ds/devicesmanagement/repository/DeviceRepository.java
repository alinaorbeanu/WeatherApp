package com.utcn.ds.devicesmanagement.repository;

import com.utcn.ds.devicesmanagement.entity.Device;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
