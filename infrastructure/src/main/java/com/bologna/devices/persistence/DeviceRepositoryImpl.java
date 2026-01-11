package com.bologna.devices.persistence;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.mapper.DeviceEntityMapper;
import com.bologna.devices.persistence.jpa.DeviceJPARepository;
import com.bologna.devices.repositories.DeviceRepository;

import java.util.List;

public class DeviceRepositoryImpl implements DeviceRepository {
    private final DeviceJPARepository deviceJPARepository;
    private final DeviceEntityMapper deviceEntityMapper;

    public DeviceRepositoryImpl(DeviceJPARepository deviceJPARepository, DeviceEntityMapper deviceEntityMapper) {
        this.deviceJPARepository = deviceJPARepository;
        this.deviceEntityMapper = deviceEntityMapper;
    }

    @Override
    public Device save(Device device) {
        var deviceEntity = deviceEntityMapper.deviceToEntity(device);
        deviceEntity = deviceJPARepository.save(deviceEntity);
        device = deviceEntityMapper.entityToDevice(deviceEntity);
        return device;
    }

    @Override
    public Device findById(Long id) {
        var deviceEntity = deviceJPARepository.findById(id);
        return deviceEntity.map(deviceEntityMapper::entityToDevice).orElse(null);
    }

    @Override
    public Device findByNameAndBrand(String name, String brand) {
        var deviceEntity = deviceJPARepository.findByNameAndBrand(name, brand);
        return deviceEntity.map(deviceEntityMapper::entityToDevice).orElse(null);
    }

    @Override
    public List<Device> findAll() {
        var deviceEntityList = deviceJPARepository.findAll();
        return deviceEntityList.stream().map(deviceEntityMapper::entityToDevice).toList();
    }

    @Override
    public List<Device> findByBrand(String brand) {
        var deviceEntityList = deviceJPARepository.findByBrand(brand);
        return deviceEntityList.stream().map(deviceEntityMapper::entityToDevice).toList();
    }

    @Override
    public List<Device> findByState(State state) {
        var deviceEntityList = deviceJPARepository.findByState(state.name());
        return deviceEntityList.stream().map(deviceEntityMapper::entityToDevice).toList();
    }

    @Override
    public void delete(Long id) {
        deviceJPARepository.deleteById(id);
    }
}
