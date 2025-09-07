package com.exalt.healthcare.domain.model.entity;

import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // SCHEDULED, COMPLETED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String notes;

    private LocalDateTime completedAt;

    @ConstructorProperties({"date", "status", "doctor", "patient", "notes"})
    public Appointment(LocalDateTime date, AppointmentStatus status, Doctor doctor, Patient patient, String notes) {
        this.date = date;
        this.status = status;
        this.doctor = doctor;
        this.patient = patient;
        this.notes = notes;
    }
}
