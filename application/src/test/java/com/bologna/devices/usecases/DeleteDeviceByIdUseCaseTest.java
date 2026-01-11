package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.exceptions.DeviceException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteDeviceByIdUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeleteDeviceByIdUseCase useCase;

    private static final LocalDateTime localDateTime = LocalDateTime.now();

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    @Test
    void given_existing_device_not_in_use_when_delete_then_deletes() {
        Long id = 1L;
        Device device = new Device(1L, "Galaxy", "Samsung", State.AVAILABLE, localDateTime);

        when(deviceRepository.findById(id)).thenReturn(device);
        doNothing().when(deviceRepository).delete(id);

        useCase.execute(id);

        verify(deviceRepository).findById(id);
        verify(deviceRepository).delete(id);
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_non_existing_device_when_delete_then_throws_device_exception_404() {
        Long id = 99L;

        when(deviceRepository.findById(id)).thenReturn(null);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(id));

        assertEquals(404, ex.getHttpCode());
        assertEquals("Device with id " + id + " does not exists", ex.getMessage());

        verify(deviceRepository).findById(id);
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_device_in_use_when_delete_then_throws_device_exception_400() {
        Long id = 2L;
        Device device = new Device(1L, "Galaxy", "Samsung", State.IN_USE, localDateTime);

        when(deviceRepository.findById(id)).thenReturn(device);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(id));

        assertEquals(400, ex.getHttpCode());
        assertEquals("Device with id " + id + " is in use and cannot be deleted", ex.getMessage());

        verify(deviceRepository).findById(id);
        verify(deviceRepository, never()).delete(anyLong());
        verifyNoMoreInteractions(deviceRepository);
    }
}