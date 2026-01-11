package com.bologna.devices.mapper;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.persistence.DeviceEntity;

public class DeviceEntityMapper {
    public DeviceEntity deviceToEntity(Device device) {
        return new DeviceEntity(
                device.id(),
                device.name(),
                device.brand(),
                device.state() != null ? device.state().name() : null,
                device.creationTime()
        );
    }

    public Device entityToDevice(DeviceEntity deviceEntity) {
        return new Device(
                deviceEntity.getId(),
                deviceEntity.getName(),
                deviceEntity.getBrand(),
                deviceEntity.getState() != null ? State.valueOf(deviceEntity.getState()) : null,
                deviceEntity.getCreationTime()
        );
    }
}
