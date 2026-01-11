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
class FindDeviceByStateUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private FindDeviceByStateUseCase useCase;

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    @Test
    void given_state_when_find_then_returns_device_list_from_repository() {
        State state = State.AVAILABLE;

        Device device1 = new Device(1L, "Galaxy", "Samsung", State.IN_USE, LocalDateTime.now());
        Device device2 = new Device(2L, "iPhone", "Apple", State.INACTIVE, LocalDateTime.now());
        List<Device> deviceList = List.of(device1, device2);

        when(deviceRepository.findByState(state)).thenReturn(deviceList);

        List<Device> result = useCase.execute(state);

        assertNotNull(result);
        assertEquals(deviceList, result);

        verify(deviceRepository).findByState(state);
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_state_when_find_returns_empty_then_returns_empty_list() {
        State state = State.IN_USE;

        List<Device> deviceList = List.of();
        when(deviceRepository.findByState(state)).thenReturn(deviceList);

        List<Device> result = useCase.execute(state);

        assertNotNull(result);
        assertEquals(deviceList, result);

        verify(deviceRepository).findByState(state);
        verifyNoMoreInteractions(deviceRepository);
    }
}
