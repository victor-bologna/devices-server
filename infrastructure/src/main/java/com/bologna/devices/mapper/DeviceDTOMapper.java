package com.bologna.devices.mapper;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.web.api.DeviceRequest;
import com.bologna.devices.web.api.DeviceResponse;

public class DeviceDTOMapper {
    public Device requestToDevice(Long id, DeviceRequest deviceRequest) {
        return new Device(
                id,
                deviceRequest.name(),
                deviceRequest.brand(),
                deviceRequest.state(),
                null
        );
    }

    public DeviceResponse deviceToResponse(Device device) {
        return new DeviceResponse(
            device.id(),
                device.name(),
                device.brand(),
                device.state(),
                device.creationTime()
        );
    }
}
