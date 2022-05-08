package com.houdaoul.teletractest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table
public class Payload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = "RecordType is mandatory")
    @JsonProperty("RecordType")
    private String recordType;

    @Column
    @NotBlank(message = "DeviceId is mandatory")
    @JsonProperty("DeviceId")
    private String deviceId;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("EventDateTime")
    private LocalDateTime eventDateTime;

    @Column
    @NotNull(message = "FieldA is mandatory")
    @JsonProperty("FieldA")
    private int fieldA;

    @Column
    @NotBlank(message = "FieldB is mandatory")
    @JsonProperty("FieldB")
    private String fieldB;

    @Column
    @NotNull(message = "FieldC is mandatory")
    @JsonProperty("FieldC")
    private double fieldC;
}
