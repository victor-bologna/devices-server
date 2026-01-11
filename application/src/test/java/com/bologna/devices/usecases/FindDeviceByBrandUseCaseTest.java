package com.bologna.devices.usecases;

import com.bologna.devices.entities.dto.Device;
import com.bologna.devices.entities.enums.State;
import com.bologna.devices.repositories.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
class FindDeviceByBrandUseCaseTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private FindDeviceByBrandUseCase useCase;

    @AfterEach
    void tearDown() {
        Mockito.reset(deviceRepository);
    }

    private static final LocalDateTime localDateTime = LocalDateTime.now();

    @Test
    void given_brand_when_find_then_returns_list_from_repository() {
        String brand = "SAMSUNG";

        Device d1 = new Device(1L, "GALAXY", "SAMSUNG", State.AVAILABLE, localDateTime);
        Device d2 = new Device(2L, "GALAXY", "SAMSUNG", State.AVAILABLE, localDateTime);
        List<Device> expected = List.of(d1, d2);

        when(deviceRepository.findByBrand(brand)).thenReturn(expected);

        List<Device> result = useCase.execute(brand);

        assertNotNull(result);
        assertEquals(expected, result);

        verify(deviceRepository).findByBrand(brand);
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void given_brand_when_find_returns_empty_then_returns_empty_list() {
        String brand = "UNKNOWN BRAND";
        List<Device> expected = List.of();

        when(deviceRepository.findByBrand(brand)).thenReturn(expected);

        List<Device> result = useCase.execute(brand);

        assertNotNull(result);
        assertEquals(expected, result);

        verify(deviceRepository).findByBrand(brand);
        verifyNoMoreInteractions(deviceRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   "})
    void given_blank_or_null_brand_when_find_then_still_delegates(String brand) {
        when(deviceRepository.findByBrand(brand)).thenReturn(List.of());

        List<Device> result = useCase.execute(brand);

        assertNotNull(result);
        assertEquals(List.of(), result);

        verify(deviceRepository).findByBrand(brand);
        verifyNoMoreInteractions(deviceRepository);
    }
}