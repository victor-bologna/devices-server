package com.bologna.devices.web;


import com.bologna.devices.entities.enums.State;
import com.bologna.devices.mapper.DeviceDTOMapper;
import com.bologna.devices.usecases.CreateDeviceUseCase;
import com.bologna.devices.usecases.DeleteDeviceByIdUseCase;
import com.bologna.devices.usecases.FindAllDevicesUseCase;
import com.bologna.devices.usecases.FindDeviceByBrandUseCase;
import com.bologna.devices.usecases.FindDeviceByIdUseCase;
import com.bologna.devices.usecases.FindDeviceByStateUseCase;
import com.bologna.devices.usecases.UpdateDeviceUseCase;
import com.bologna.devices.web.api.DeviceRequest;
import com.bologna.devices.web.api.DeviceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Devices", description = "API to manage devices")
@RestController
@RequestMapping("v1/devices")
public class DeviceController {
    private final CreateDeviceUseCase createDeviceUseCase;
    private final DeleteDeviceByIdUseCase deleteDeviceByIdUseCase;
    private final FindAllDevicesUseCase findAllDevicesUseCase;
    private final FindDeviceByBrandUseCase findDeviceByBrandUseCase;
    private final FindDeviceByIdUseCase findDeviceByIdUseCase;
    private final FindDeviceByStateUseCase findDeviceByStateUseCase;
    private final UpdateDeviceUseCase updateDeviceUseCase;
    private final DeviceDTOMapper deviceDTOMapper;

    public DeviceController(CreateDeviceUseCase createDeviceUseCase,
                            DeleteDeviceByIdUseCase deleteDeviceByIdUseCase,
                            FindAllDevicesUseCase findAllDevicesUseCase,
                            FindDeviceByBrandUseCase findDeviceByBrandUseCase,
                            FindDeviceByIdUseCase findDeviceByIdUseCase,
                            FindDeviceByStateUseCase findDeviceByStateUseCase,
                            UpdateDeviceUseCase updateDeviceUseCase,
                            DeviceDTOMapper deviceDTOMapper) {
        this.createDeviceUseCase = createDeviceUseCase;
        this.deleteDeviceByIdUseCase = deleteDeviceByIdUseCase;
        this.findAllDevicesUseCase = findAllDevicesUseCase;
        this.findDeviceByBrandUseCase = findDeviceByBrandUseCase;
        this.findDeviceByIdUseCase = findDeviceByIdUseCase;
        this.findDeviceByStateUseCase = findDeviceByStateUseCase;
        this.updateDeviceUseCase = updateDeviceUseCase;
        this.deviceDTOMapper = deviceDTOMapper;
    }

    @Operation(summary = "Create a new device if name and brand does not exists in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device created"),
            @ApiResponse(responseCode = "400", description = "Fields missing.")
    })
    @PostMapping
    public ResponseEntity<DeviceResponse> postDevice(@RequestBody DeviceRequest deviceRequest) {
        var device = deviceDTOMapper.requestToDevice(null, deviceRequest);
        device = createDeviceUseCase.execute(device);
        var deviceResponse = deviceDTOMapper.deviceToResponse(device);
        return ResponseEntity.status(201).body(deviceResponse);
    }

    @Operation(summary = "Delete device by id if exists in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted"),
            @ApiResponse(responseCode = "400", description = "Device is in use and cannot be deleted."),
            @ApiResponse(responseCode = "404", description = "Could not find device to delete.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") Long id) {
        deleteDeviceByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lists all devices in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lists all devices")
    })
    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        var deviceList = findAllDevicesUseCase.execute();
        var deviceResponseList = deviceList.stream().map(deviceDTOMapper::deviceToResponse).toList();
        return ResponseEntity.ok(deviceResponseList);
    }

    @Operation(summary = "Find device by brand if exists in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return devices found."),
            @ApiResponse(responseCode = "204", description = "No device found.")
    })
    @GetMapping("/brand")
    public ResponseEntity<List<DeviceResponse>> getDeviceByBrand(@RequestParam("brand") String brand) {
        var deviceList = findDeviceByBrandUseCase.execute(brand);
        if (deviceList.isEmpty()) { return ResponseEntity.noContent().build(); }
        var deviceResponseList = deviceList.stream().map(deviceDTOMapper::deviceToResponse).toList();
        return ResponseEntity.ok(deviceResponseList);
    }

    @Operation(summary = "Find device by state if exists in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return devices found."),
            @ApiResponse(responseCode = "204", description = "No device found.")
    })
    @GetMapping("/state")
    public ResponseEntity<List<DeviceResponse>> getDeviceByState(@RequestParam("state") State state) {
        var deviceList = findDeviceByStateUseCase.execute(state);
        if (deviceList.isEmpty()) { return ResponseEntity.noContent().build(); }
        var deviceResponseList = deviceList.stream().map(deviceDTOMapper::deviceToResponse).toList();
        return ResponseEntity.ok(deviceResponseList);
    }

    @Operation(summary = "Find a device by id if exists in database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return device."),
            @ApiResponse(responseCode = "404", description = "No device found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable("id") Long id) {
        var device = findDeviceByIdUseCase.execute(id);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        var deviceResponse = deviceDTOMapper.deviceToResponse(device);
        return ResponseEntity.ok(deviceResponse);
    }

    @Operation(summary = "Update device fields by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated device."),
            @ApiResponse(responseCode = "204", description = "No device found."),
            @ApiResponse(responseCode = "400", description = "Device is in use and cannot be updated."),
    })
    @PatchMapping( "/{id}")
    public ResponseEntity<DeviceResponse> patchDevice(@PathVariable("id") Long id, @RequestBody DeviceRequest deviceRequest) {
        var device = deviceDTOMapper.requestToDevice(id, deviceRequest);
        device = updateDeviceUseCase.execute(device);
        var deviceResponse = deviceDTOMapper.deviceToResponse(device);
        return ResponseEntity.ok(deviceResponse);
    }
}
