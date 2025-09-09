package com.exalt.healthcare.domain.model.entity;

import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // SCHEDULED, COMPLETED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String notes;

    @ConstructorProperties({"date", "endTime", "startTime", "status", "doctor", "patient", "notes"})
    public Appointment(LocalDate date, LocalDateTime startTime, LocalDateTime endTime, AppointmentStatus status, Doctor doctor, Patient patient, String notes) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.doctor = doctor;
        this.patient = patient;
        this.notes = notes;
    }
}
