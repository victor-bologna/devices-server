package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindDeviceByIdUseCase {
    private static final Logger logger = LoggerFactory.getLogger(FindDeviceByIdUseCase.class);
    private final DeviceRepository deviceRepository;

    public FindDeviceByIdUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device execute(Long id) {
        logger.info("class={} m=execute s=start id={}", this.getClass().getSimpleName(), id);
        try {
            return deviceRepository.findById(id);
        } catch (Exception e) {
            logger.error("class={} m=execute s=error id={} message={}", this.getClass().getSimpleName(), id, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish id={}", this.getClass().getSimpleName(), id);
        }
    }
}
