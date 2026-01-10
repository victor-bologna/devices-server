package com.bologna.devices.ports;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;

import java.util.List;

public interface DevicePort {
    Device create(Device device);
    Device update(Device device);
    Device findById(Long id);
    List<Device> findAll();
    List<Device> findByBrand(String brand);
    List<Device> findByState(State state);
    void delete();
}
