package com.utcn.ds.devicesmanagement.entity;

import jakarta.persistence.Column;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDeviceMappingFK implements Serializable {

    @Column(
            name = "user_id"
    )
    private Long userId;
    @Column(
            name = "device_id"
    )
    private Long deviceId;
}
