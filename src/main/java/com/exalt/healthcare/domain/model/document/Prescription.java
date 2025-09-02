package com.exalt.healthcare.domain.model.document;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Document(collection = "Prescription")
public class Prescription {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private long id;

    @NotBlank
    private Long patient_id;

    @NotBlank
    private Long doctor_id;

    private String [] medications;

    private String [] notes;

    private LocalDate date;

    @ConstructorProperties({"patient_id", "doctor_id", "date", "notes", "medications"})
    public Prescription(Long patient_id, Long doctor_id, LocalDate date, String [] notes, String[] medications) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.date = date;
        this.notes = notes;
        this.medications = medications;
    }
}
