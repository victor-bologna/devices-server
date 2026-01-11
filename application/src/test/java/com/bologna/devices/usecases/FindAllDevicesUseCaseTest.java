package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.repositories.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllDevicesUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private FindAllDevicesUseCase useCase;

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    private static final LocalDateTime localDateTime = LocalDateTime.now();

    @Test
    void when_find_all_then_returns_devices_list() {
        Device d1 = new Device(1L, "Galaxy", "Samsung", State.AVAILABLE, localDateTime);
        Device d2 = new Device(2L, "Galaxy", "Samsung", State.AVAILABLE, localDateTime);
        List<Device> deviceList = List.of(d1, d2);

        when(deviceRepository.findAll()).thenReturn(deviceList);

        List<Device> result = useCase.execute();

        assertNotNull(result);
        assertEquals(deviceList, result);

        verify(deviceRepository).findAll();
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void when_find_all_returns_empty_then_returns_empty_list() {
        List<Device> deviceList = List.of();

        when(deviceRepository.findAll()).thenReturn(deviceList);

        List<Device> result = useCase.execute();

        assertNotNull(result);
        assertEquals(deviceList, result);

        verify(deviceRepository).findAll();
        verifyNoMoreInteractions(deviceRepository);
    }
}
