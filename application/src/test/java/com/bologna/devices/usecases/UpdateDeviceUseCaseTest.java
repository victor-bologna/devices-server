package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.exceptions.DeviceException;
import com.bologna.devices.repositories.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDeviceUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private UpdateDeviceUseCase useCase;

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    @Test
    void given_all_null_fields_when_update_then_throws_device_exception_400() {
        Device input = new Device(1L, null, null, null, null);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(input));

        assertEquals(400, ex.getHttpCode());
        assertEquals("At least 1 field must not be null to be modified", ex.getMessage());

        verifyNoInteractions(deviceRepository);
    }

    @Test
    void given_non_existing_id_when_update_then_throws_device_exception_404() {
        Device input = new Device(99L, "GALAXY", null, null, null);

        when(deviceRepository.findById(99L)).thenReturn(null);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(input));

        assertEquals(404, ex.getHttpCode());
        assertEquals("Device with id 99 does not exists", ex.getMessage());

        verify(deviceRepository).findById(99L);
        verifyNoMoreInteractions(deviceRepository);
    }

    @ParameterizedTest
    @CsvSource(value = {"iPhone, null, name", "null, Apple, brand"}, nullValues = {"null"})
    void given_device_in_use_when_update_brand_then_throws_device_exception_400(String name, String brand, String errorName) {
        LocalDateTime creationTime = LocalDateTime.now();

        Device fromDb = new Device(1L, "GALAXY", "SAMSUNG", State.IN_USE, creationTime);
        Device input = new Device(1L, name, brand, null, null);

        when(deviceRepository.findById(1L)).thenReturn(fromDb);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(input));

        assertEquals(400, ex.getHttpCode());
        assertEquals("Device with id 1 is in use and " + errorName + " cannot be updated", ex.getMessage());

        verify(deviceRepository).findById(1L);
        verify(deviceRepository, never()).save(any());
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_device_not_in_use_when_update_partial_fields_then_merges_and_saves() {
        LocalDateTime creationTime = LocalDateTime.now();

        Device fromDb = new Device(1L, "GALAXY", "SAMSUNG", State.AVAILABLE, creationTime);
        Device input = new Device(1L, "GALAXY S24", null, State.IN_USE, null);

        when(deviceRepository.findById(1L)).thenReturn(fromDb);

        ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);

        when(deviceRepository.save(any(Device.class))).thenAnswer(inv -> inv.getArgument(0));

        Device saved = useCase.execute(input);

        assertNotNull(saved);
        assertEquals(input.id(), saved.id());
        assertEquals("GALAXY S24", saved.name());
        assertEquals("SAMSUNG", saved.brand());
        assertEquals(State.IN_USE, saved.state());
        assertEquals(creationTime, saved.creationTime());

        verify(deviceRepository).findById(1L);
        verify(deviceRepository).save(captor.capture());
        verifyNoMoreInteractions(deviceRepository);

        Device mergedSentToSave = captor.getValue();
        assertEquals(1L, mergedSentToSave.id());
        assertEquals("GALAXY S24", mergedSentToSave.name());
        assertEquals("SAMSUNG", mergedSentToSave.brand());
        assertEquals(State.IN_USE, mergedSentToSave.state());
        assertEquals(creationTime, mergedSentToSave.creationTime());
    }
}
