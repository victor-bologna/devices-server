package com.bologna.devices.entities.dto;

import com.bologna.devices.entities.enums.State;

import java.time.LocalDateTime;

public record Device(
  Long id,
  String name,
  String brand,
  State state,
  LocalDateTime creationTime
) {
    public boolean checkIfDeviceInUse() {
        return state == State.IN_USE;
    }
}
