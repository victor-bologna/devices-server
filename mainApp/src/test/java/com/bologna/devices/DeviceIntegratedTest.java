package com.bologna.devices;

import com.bologna.devices.entities.enums.State;
import com.bologna.devices.web.api.DeviceRequest;
import com.bologna.devices.web.api.DeviceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeviceIntegratedTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_create_get_all_get_by_brand_get_by_state_get_by_id_update_and_delete() throws Exception {
        DeviceRequest req1 = new DeviceRequest("GALAXY", "SAMSUNG", State.AVAILABLE);

        String body1 = mockMvc.perform(post("/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("GALAXY"))
                .andExpect(jsonPath("$.brand").value("SAMSUNG"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.creationTime").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var deviceResponse1 = objectMapper.readValue(body1, DeviceResponse.class);

        DeviceRequest req2 = new DeviceRequest("IPHONE", "APPLE", State.IN_USE);

        mockMvc.perform(post("/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.state").value("IN_USE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/v1/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));

        mockMvc.perform(get("/v1/devices/brand").param("brand", "SAMSUNG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("SAMSUNG"));

        mockMvc.perform(get("/v1/devices/state").param("state", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state").value("AVAILABLE"));

        mockMvc.perform(get("/v1/devices/{id}", deviceResponse1.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceResponse1.id()))
                .andExpect(jsonPath("$.name").value(deviceResponse1.name()));

        DeviceRequest patchReq = new DeviceRequest("GALAXY S24", null, State.INACTIVE);

        mockMvc.perform(patch("/v1/devices/{id}", deviceResponse1.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceResponse1.id()))
                .andExpect(jsonPath("$.name").value("GALAXY S24"))
                .andExpect(jsonPath("$.brand").value(deviceResponse1.brand()))
                .andExpect(jsonPath("$.state").value("INACTIVE"))
                .andExpect(jsonPath("$.creationTime").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(delete("/v1/devices/{id}", deviceResponse1.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/devices/{id}", deviceResponse1.id()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/v1/devices/brand").param("brand", "Unknown"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/devices/state").param("state", "AVAILABLE"))
                .andExpect(status().isNoContent());
    }
}
