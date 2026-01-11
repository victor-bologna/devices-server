package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FindDeviceByStateUseCase {
    private static final Logger logger = LoggerFactory.getLogger(FindDeviceByStateUseCase.class);
    private final DeviceRepository deviceRepository;

    public FindDeviceByStateUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> execute(State state) {
        logger.info("class={} m=execute s=start state={}", this.getClass().getSimpleName(), state);
        try {
            return deviceRepository.findByState(state);
        } catch (Exception e) {
            logger.error("class={} m=execute s=error state={} message={}", this.getClass().getSimpleName(), state, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish state={}", this.getClass().getSimpleName(), state);
        }
    }
}
