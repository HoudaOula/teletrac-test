package com.houdaoul.teletractest.service;

import com.houdaoul.teletractest.domain.Payload;
import com.houdaoul.teletractest.repository.PayloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PayloadService {

    private static final Logger logger = LoggerFactory.getLogger(PayloadService.class);

    private final PayloadRepository payloadRepository;

    public PayloadService(PayloadRepository payloadRepository) {
        this.payloadRepository = payloadRepository;
    }

    public Payload savePayload(Payload payload) {
        return payloadRepository.save(payload);
    }

    public Optional<Payload> getPayload(long id) {
        return payloadRepository.findById(id);
    }

    public List<Payload> getAllPayloads() {
        return StreamSupport.stream(payloadRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
