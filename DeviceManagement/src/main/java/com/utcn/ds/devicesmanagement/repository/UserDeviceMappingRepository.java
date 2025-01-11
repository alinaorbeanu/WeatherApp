package com.utcn.ds.devicesmanagement.repository;

import com.utcn.ds.devicesmanagement.entity.Device;
import com.utcn.ds.devicesmanagement.entity.UserDeviceMapping;
import com.utcn.ds.devicesmanagement.entity.UserDeviceMappingFK;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserDeviceMappingRepository extends JpaRepository<UserDeviceMapping, UserDeviceMappingFK> {

    List<UserDeviceMapping> getAllByUserId(Long userId);

    Optional<UserDeviceMapping> findByDeviceId(Long deviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserDeviceMapping ud WHERE ud.userId = :userId AND ud.deviceId = :deviceId")
    int deleteByUserIdAndDeviceId(@Param("userId") Long userId, @Param("deviceId") Long deviceId);
}
