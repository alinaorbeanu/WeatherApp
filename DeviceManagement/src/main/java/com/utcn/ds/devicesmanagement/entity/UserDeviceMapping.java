package com.utcn.ds.devicesmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userdevice")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserDeviceMappingFK.class)
public class UserDeviceMapping implements Serializable {

    @Id
    private Long userId;

    @Id
    private Long deviceId;

    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private Device device;

    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private User user;
}
