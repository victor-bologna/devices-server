package com.bologna.devices.config;

import com.bologna.devices.mapper.DeviceDTOMapper;
import com.bologna.devices.mapper.DeviceEntityMapper;
import com.bologna.devices.persistence.DeviceRepositoryImpl;
import com.bologna.devices.persistence.jpa.DeviceJPARepository;
import com.bologna.devices.repositories.DeviceRepository;
import com.bologna.devices.usecases.CreateDeviceUseCase;
import com.bologna.devices.usecases.DeleteDeviceByIdUseCase;
import com.bologna.devices.usecases.FindAllDevicesUseCase;
import com.bologna.devices.usecases.FindDeviceByBrandUseCase;
import com.bologna.devices.usecases.FindDeviceByIdUseCase;
import com.bologna.devices.usecases.FindDeviceByStateUseCase;
import com.bologna.devices.usecases.UpdateDeviceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceConfig {
    private final DeviceJPARepository deviceJPARepository;

    public DeviceConfig(DeviceJPARepository deviceJPARepository) {
        this.deviceJPARepository = deviceJPARepository;
    }

    @Bean
    DeviceRepository deviceRepository() {
        return new DeviceRepositoryImpl(deviceJPARepository, deviceEntityMapper());
    }

    @Bean
    CreateDeviceUseCase createDeviceUseCase(DeviceRepository deviceRepository) {
        return new CreateDeviceUseCase(deviceRepository);
    }

    @Bean
    DeleteDeviceByIdUseCase deleteDeviceByIdUseCase(DeviceRepository deviceRepository) {
        return new DeleteDeviceByIdUseCase(deviceRepository);
    }

    @Bean
    FindAllDevicesUseCase findAllDevicesUseCase(DeviceRepository deviceRepository) {
        return new FindAllDevicesUseCase(deviceRepository);
    }

    @Bean
    FindDeviceByBrandUseCase findDeviceByBrandUseCase(DeviceRepository deviceRepository) {
        return new FindDeviceByBrandUseCase(deviceRepository);
    }

    @Bean
    FindDeviceByIdUseCase findDeviceByIdUseCase(DeviceRepository deviceRepository) {
        return new FindDeviceByIdUseCase(deviceRepository);
    }

    @Bean
    FindDeviceByStateUseCase findDeviceByStateUseCase(DeviceRepository deviceRepository) {
        return new FindDeviceByStateUseCase(deviceRepository);
    }

    @Bean
    UpdateDeviceUseCase updateDeviceUseCase(DeviceRepository deviceRepository) {
        return new UpdateDeviceUseCase(deviceRepository);
    }

    @Bean
    DeviceDTOMapper deviceDTOMapper() {
        return new DeviceDTOMapper();
    }

    @Bean
    DeviceEntityMapper deviceEntityMapper() {
        return new DeviceEntityMapper();
    }
}
