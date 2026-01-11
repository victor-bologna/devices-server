package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.exceptions.DeviceException;
import com.bologna.devices.repositories.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CreateDeviceUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private CreateDeviceUseCase useCase;

    private static final LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeAll
    static void setUp() {
        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(localDateTime);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    @Test
    void given_valid_device_when_create_then_returns_saved_device() {
        Device input = new Device(null, "GALAXY", "SAMSUNG", State.AVAILABLE, localDateTime);
        Device savedInput = new Device(1L, "GALAXY", "SAMSUNG", State.AVAILABLE, localDateTime);

        when(deviceRepository.findByNameAndBrand("GALAXY", "SAMSUNG")).thenReturn(null);
        when(deviceRepository.save(input)).thenReturn(savedInput);

        Device saved = useCase.execute(input);

        assertNotNull(saved);
        assertEquals("GALAXY", saved.name());
        assertEquals("SAMSUNG", saved.brand());
        assertEquals(State.AVAILABLE, saved.state());
        assertNotNull(saved.creationTime());

        verify(deviceRepository).save(input);
        assertNotNull(savedInput.creationTime());

        verify(deviceRepository).findByNameAndBrand("GALAXY", "SAMSUNG");
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_same_name_when_create_then_throws_device_exception() {
        Device input = new Device(null, "GALAXY", "SAMSUNG", State.AVAILABLE, null);
        when(deviceRepository.findByNameAndBrand("GALAXY", "SAMSUNG"))
                .thenReturn(new Device(1L, "GALAXY", "SAMSUNG", State.AVAILABLE, localDateTime));

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(input));

        assertEquals(400, ex.getHttpCode());
        assertEquals("Device with name " + input.name() + " already exists.", ex.getMessage());

        verify(deviceRepository).findByNameAndBrand("GALAXY", "SAMSUNG");
        verifyNoMoreInteractions(deviceRepository);
    }

    @ParameterizedTest
    @CsvSource(value = {"null, SAMSUNG, AVAILABLE, name", "GALAXY, null, AVAILABLE, brand", "GALAXY, SAMSUNG, null, state"}, nullValues = "null")
    void given_null_inputs_when_create_then_throws_device_exception(String name, String brand, String state, String errorField) {
        var stateEnum = state != null ? State.valueOf(state) : null;
        Device input = new Device(null, name, brand, stateEnum, null);

        DeviceException ex = assertThrows(DeviceException.class, () -> useCase.execute(input));

        assertEquals(400, ex.getHttpCode());
        assertEquals("Device with " + errorField + " must not be null.", ex.getMessage());

        verifyNoInteractions(deviceRepository);
    }
}