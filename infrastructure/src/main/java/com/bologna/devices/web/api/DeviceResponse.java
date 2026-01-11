package com.bologna.devices.web.api;

import com.bologna.devices.entities.enums.State;

import java.time.LocalDateTime;

public record DeviceResponse(
        Long id,
        String name,
        String brand,
        State state,
        LocalDateTime creationTime
) {
}
