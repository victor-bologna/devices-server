package com.bologna.devices.web.api;

import com.bologna.devices.entities.enums.State;

public record DeviceRequest(
        String name,
        String brand,
        State state
) {
}
