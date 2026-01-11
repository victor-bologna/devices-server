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
    public boolean checkIfNameChanged(String name) {
        return name != null && !name.equalsIgnoreCase(this.name);
    }
    public boolean checkIfBrandChanged(String brand) {
        return brand != null && !brand.equalsIgnoreCase(this.brand);
    }
}
