package com.bologna.devices.usecases;

import com.bologna.devices.exceptions.DeviceException;
import com.bologna.devices.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteDeviceByIdUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DeleteDeviceByIdUseCase.class.getName());
    private final DeviceRepository deviceRepository;

    public DeleteDeviceByIdUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void execute(Long id) {
        logger.info("class={} m=execute s=start id={}", this.getClass().getSimpleName(), id);
        try {
            var device = deviceRepository.findById(id);
            if (device == null) {
                throw new DeviceException("Device with id " + id + " does not exists", 404);
            }
            if (device.checkIfDeviceInUse()) {
                throw new DeviceException("Device with id " + id + " is in use and cannot be deleted", 400);
            }
            deviceRepository.delete(id);
        } catch (Exception e) {
            logger.error("class={} m=execute s=error id={} message={}", this.getClass().getSimpleName(), id, e.getMessage());
            throw e;
        } finally {
            logger.info("class={} m=execute s=finish id={}", this.getClass().getSimpleName(), id);
        }
    }
}
