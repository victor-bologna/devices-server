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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindDeviceByIdUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private FindDeviceByIdUseCase useCase;

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    @Test
    void given_existing_id_when_find_then_returns_device() {
        final LocalDateTime localDateTime = LocalDateTime.now();
        Long id = 1L;
        Device device = new Device(1L, "Galaxy", "Samsung", State.AVAILABLE, localDateTime);

        when(deviceRepository.findById(id)).thenReturn(device);

        Device result = useCase.execute(id);

        assertEquals(device, result);

        verify(deviceRepository).findById(id);
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_non_existing_id_when_find_then_returns_null() {
        Long id = 999L;

        when(deviceRepository.findById(id)).thenReturn(null);

        Device result = useCase.execute(id);

        assertNull(result);

        verify(deviceRepository).findById(id);
        verifyNoMoreInteractions(deviceRepository);
    }
}
