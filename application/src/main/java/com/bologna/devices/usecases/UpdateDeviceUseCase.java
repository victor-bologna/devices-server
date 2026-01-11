package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.exceptions.DeviceException;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateDeviceUseCase {
    private static final Logger logger = LoggerFactory.getLogger(UpdateDeviceUseCase.class);
    private final DeviceRepository deviceRepository;

    public UpdateDeviceUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device execute(Device device) {
        logger.info("class={} m=execute s=start device={}", this.getClass().getSimpleName(), device);
        try {
            validateDeviceInput(device);
            var foundDevice = deviceRepository.findById(device.id());
            if (foundDevice == null) {
                throw new DeviceException("Device with id " + device.id() + " does not exists", 204);
            }
            validateDevice(device, foundDevice);
            device = mergeDevice(foundDevice, device);
            return deviceRepository.save(device);
        } catch (Exception e) {
            logger.error("class={} m=execute s=error device={} message={}", this.getClass().getSimpleName(), device, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish device={}", this.getClass().getSimpleName(), device);
        }
    }

    private void validateDeviceInput(Device device) {
        if (device.brand() == null && device.name() == null && device.state() == null) {
            throw new DeviceException("At least 1 field must not be null to be modified", 400);
        }
    }

    private static void validateDevice(Device device, Device foundDevice) {
        if (foundDevice.checkIfDeviceInUse()) {
            if (foundDevice.checkIfBrandChanged(device.brand())) {
                throw new DeviceException("Device with id " + device.id() + " is in use and brand cannot be updated", 400);
            } else if (foundDevice.checkIfNameChanged(device.name())) {
                throw new DeviceException("Device with id " + device.id() + " is in use and name cannot be updated", 400);
            }
        }
    }

    public Device mergeDevice(Device fromDb, Device input) {
        return new Device(
                fromDb.id(),
                input.name() != null ? input.name().toUpperCase() : fromDb.name(),
                input.brand() != null ? input.brand().toUpperCase() : fromDb.brand(),
                input.state() != null ? input.state() : fromDb.state(),
                fromDb.creationTime()
        );
    }
}
