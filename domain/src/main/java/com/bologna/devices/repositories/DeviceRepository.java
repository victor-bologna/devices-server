package com.bologna.devices.repositories;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;

import java.util.List;

public interface DeviceRepository {
    Device save(Device device);
    Device findById(Long id);
    Device findByNameAndBrand(String name, String brand);
    List<Device> findAll();
    List<Device> findByBrand(String brand);
    List<Device> findByState(State state);
    void delete(Long id);
}
