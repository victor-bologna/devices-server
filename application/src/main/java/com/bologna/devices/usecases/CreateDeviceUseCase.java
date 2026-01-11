package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.exceptions.DeviceException;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CreateDeviceUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateDeviceUseCase.class);
    private final DeviceRepository deviceRepository;

    public CreateDeviceUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device execute(Device device) {
        logger.info("class={} m=execute s=start device={}", this.getClass().getSimpleName(), device);
        try {
            validateDeviceInput(device);
            var deviceExistent = deviceRepository.findByNameAndBrand(
                    device.name().toUpperCase(),
                    device.brand().toUpperCase()
            );
            if (deviceExistent == null) {
                device = new Device(
                        device.id(), device.name().toUpperCase(), device.brand().toUpperCase(), device.state(), LocalDateTime.now()
                );
                return deviceRepository.save(device);
            } else {
                throw new DeviceException("Device with name " + device.name() + " already exists.", 400);
            }
        } catch (Exception e) {
            logger.error("class={} m=execute s=error device={} message={}", this.getClass().getSimpleName(), device, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish device={}", this.getClass().getSimpleName(), device);
        }
    }

    private void validateDeviceInput(Device device) {
        if (device.brand() == null) {
            throw new DeviceException("Device with brand must not be null.", 400);
        }
        if (device.state() == null) {
            throw new DeviceException("Device with state must not be null.", 400);
        }
        if (device.name() == null) {
            throw new DeviceException("Device with name must not be null.", 400);
        }
    }
}
