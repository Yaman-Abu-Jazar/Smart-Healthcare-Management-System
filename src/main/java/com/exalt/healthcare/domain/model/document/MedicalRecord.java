package com.exalt.healthcare.domain.model.document;

import com.exalt.healthcare.domain.valueobject.MedicalRecordStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Document(collection = "medical_records")
public class MedicalRecord {

    @Transient
    public static final String SEQUENCE_NAME = "records_sequence";

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

    @Field("record_date")
    private LocalDateTime createdAt;

    @Field("update_date")
    private LocalDateTime updatedAt;

    @Field("status")
    private MedicalRecordStatus status;

    @Field("lab_results")
    private List<String> labResults;

    @ConstructorProperties({"patientId", "doctorId", "medications", "notes", "createdAt", "updatedAt", "status", "labResults"})
    public MedicalRecord(Long patientId, List<String> medications, Long doctorId, List<String> notes, LocalDateTime createdAt, LocalDateTime updatedAt, MedicalRecordStatus status, List<String> labResults) {
        this.patientId = patientId;
        this.medications = medications;
        this.doctorId = doctorId;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.labResults = labResults;
    }
}
