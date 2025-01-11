package com.utcn.ds.devicesmanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "device")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;

    @Column(
            name = "deviceNumber",
            nullable = false
    )
    private Long deviceNumber;

    @Column(
            name = "energyProduction",
            nullable = false
    )
    private Long maximumEnergyProduction;

    @OneToMany(
            mappedBy = "device",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<UserDeviceMapping> userDeviceMappings;
}
