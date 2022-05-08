package com.houdaoul.teletractest.controller;

import com.houdaoul.teletractest.domain.Device;
import com.houdaoul.teletractest.domain.Payload;
import com.houdaoul.teletractest.service.PayloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/payloads")
@RestController
public class PayloadController {

    private static final Logger logger = LoggerFactory.getLogger(PayloadController.class);

    private final PayloadService payloadService;

    public PayloadController(PayloadService payloadService) {
        this.payloadService = payloadService;
    }

    @PostMapping("/echo")
    public ResponseEntity<Payload> addPayloadEcho(@Valid @RequestBody Payload payload) {
        logger.info("Request to /api/payloads/echo");
        Payload p = payloadService.savePayload(payload);
        return ResponseEntity.ok().body(p);
    }

    @PostMapping("/nocontent")
    public ResponseEntity<?> addPayloadNoContent(@Valid @RequestBody Payload payload) {
        logger.info("Request to /api/payloads/nocontent");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/device")
    public ResponseEntity<Device> addPayloadDevice(@Valid @RequestBody Payload payload) {
        logger.info("Request to /api/payloads/device");
        Payload p = payloadService.savePayload(payload);
        return ResponseEntity.ok().body(Device.builder().deviceID(p.getDeviceId()).build());
    }

    @PostMapping
    public ResponseEntity<Payload> addPayload(@Valid @RequestBody Payload payload) {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Payload>> getAllPayloads() {
        return ResponseEntity.ok().body(payloadService.getAllPayloads());
    }
}
