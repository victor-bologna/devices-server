package com.bologna.devices.persistence.jpa;

import com.bologna.devices.persistence.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceJPARepository extends JpaRepository<DeviceEntity, Long> {
    Optional<DeviceEntity> findByNameAndBrand(String name, String brand);

    List<DeviceEntity> findByBrand(String brand);

    List<DeviceEntity> findByState(String name);
}
