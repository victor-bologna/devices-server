package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FindAllDevicesUseCase {
    private static final Logger logger = LoggerFactory.getLogger(FindAllDevicesUseCase.class);
    private final DeviceRepository deviceRepository;

    public FindAllDevicesUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> execute() {
        logger.info("class={} m=execute s=start", this.getClass().getSimpleName());
        try {
            return deviceRepository.findAll();
        } catch (Exception e) {
            logger.error("class={} m=execute s=error message={}", this.getClass().getSimpleName(), e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish", this.getClass().getSimpleName());
        }
    }
}
