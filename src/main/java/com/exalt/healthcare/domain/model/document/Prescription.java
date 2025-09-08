package com.exalt.healthcare.domain.model.document;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Document(collection = "prescriptions")
public class Prescription {

    @Transient
    public static final String SEQUENCE_NAME = "prescriptions_sequence";

    @Id
    private long id;

    @NotNull
    @Field("patient_id")
    private Long patientId;

    @NotNull
    @Field("doctor_id")
    private Long doctorId;

    private List<String> medications;

    private List<String> notes;

    @Field("prescription_date")
    private LocalDateTime createdAt;

    @ConstructorProperties({"patientId", "doctorId", "createdAt", "notes", "medications"})
    public Prescription(Long patientId, Long doctorId, LocalDateTime createdAt, List<String> notes, List<String> medications) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createdAt = createdAt;
        this.notes = notes;
        this.medications = medications;
    }
}
