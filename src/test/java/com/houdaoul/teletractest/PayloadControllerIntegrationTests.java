package com.houdaoul.teletractest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.houdaoul.teletractest.domain.Payload;
import com.houdaoul.teletractest.service.PayloadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PayloadControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Mock
    private PayloadService payloadService;

    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsBytes(object);
    }

    private Payload generatePayload() {
        Payload payload = Payload.builder()
                .recordType("xxx")
                .deviceId("357370040159770")
                .eventDateTime(LocalDateTime.parse("2014-05-12T05:09:48Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
                .fieldA(68)
                .fieldB("xxx")
                .fieldC(123.45)
                .build();
        return payload;
    }

    private Payload generateBadPayload() {
        Payload payload = Payload.builder()
                .recordType("xxx")
                .deviceId("357370040159770")
                .eventDateTime(LocalDateTime.parse("2014-05-12T05:09", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                .fieldA(68)
                .fieldB("")
                .fieldC(123.45)
                .build();
        return payload;
    }

    @Test
    public void testWhenNoContentThenReturn204() throws Exception {
        Payload payload = generatePayload();

        when(payloadService.savePayload(payload)).thenReturn(payload);

        this.mockMvc.perform(post("/api/payloads/nocontent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(payload)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testWithBadRequestDataWhenEchoThenReturn400() throws Exception {
        Payload payload = generateBadPayload();

        when(payloadService.savePayload(payload)).thenReturn(payload);
        this.mockMvc.perform(post("/api/payloads/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhenEchoThenReturn200WithPayload() throws Exception {
        Payload payload = generatePayload();

        when(payloadService.savePayload(payload)).thenReturn(payload);
        this.mockMvc.perform(post("/api/payloads/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RecordType").value(payload.getRecordType()))
                .andExpect(jsonPath("$.DeviceId").value(payload.getDeviceId()))
                .andExpect(jsonPath("$.EventDateTime").value(payload.getEventDateTime().toString()))
                .andExpect(jsonPath("$.FieldA").value(payload.getFieldA()))
                .andExpect(jsonPath("$.FieldB").value(payload.getFieldB()))
                .andExpect(jsonPath("$.FieldC").value(payload.getFieldC()));
    }

    @Test
    public void testWhenDeviceThenReturn200WithDeviceId() throws Exception {
        Payload payload = generatePayload();

        when(payloadService.savePayload(payload)).thenReturn(payload);
        this.mockMvc.perform(post("/api/payloads/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.DeviceId").value(payload.getDeviceId()));
    }

    @Test
    public void testWhenOtherThenReturn400() throws Exception {
        Payload payload = generatePayload();

        when(payloadService.savePayload(payload)).thenReturn(payload);
        this.mockMvc.perform(post("/api/payloads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(payload)))
                .andExpect(status().isBadRequest());
    }
}
