package com.exalt.healthcare.domain.model.document;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Transient;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Document(collection = "MedicalRecords")
public class MedicalRecord {

    @Transient
    public static final String SEQUENCE_NAME = "records_sequence";

    @Id
    private long id;

    @NotNull
    private Long patient_id;

    @NotNull
    private Long doctor_id;

    private String [] medications;

    private String [] notes;

    private LocalDate date;
}
