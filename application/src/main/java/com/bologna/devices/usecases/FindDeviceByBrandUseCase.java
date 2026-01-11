package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FindDeviceByBrandUseCase {
    private static final Logger logger = LoggerFactory.getLogger(FindDeviceByBrandUseCase.class);
    private final DeviceRepository deviceRepository;

    public FindDeviceByBrandUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> execute(String brand) {
        logger.info("class={} m=execute s=start brand={}", this.getClass().getSimpleName(), brand);
        try {
            return deviceRepository.findByBrand(brand.toUpperCase());
        } catch (RuntimeException e) {
            logger.error("class={} m=execute s=error brand={} message={}", this.getClass().getSimpleName(), brand, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish brand={}", this.getClass().getSimpleName(), brand);
        }
    }
}
